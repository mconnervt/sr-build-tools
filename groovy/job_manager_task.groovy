import jenkins.model.Jenkins
import java.io.FileInputStream
import javax.xml.transform.stream.StreamSource
import hudson.plugins.git.browser.GithubWeb

def repositories = [
        "https://github.com/shadow-robot/sr-visualization.git",
        "https://github.com/shadow-robot/sr_vision.git",
        "https://github.com/shadow-robot/sr_tools.git",
        "https://github.com/shadow-robot/sr_standalone.git",
        "https://github.com/shadow-robot/sr-ros-interface.git",
        "https://github.com/shadow-robot/sr-ros-interface-ethercat.git",
        "https://github.com/shadow-robot/sr-ronex.git",
        "https://github.com/shadow-robot/ramcip.git",
        "https://github.com/shadow-robot/build-servers-check.git",
        "https://github.com/shadow-robot/sr_interface.git",
        "https://github.com/shadow-robot/sr_core.git",
        "https://github.com/shadow-robot/sr_common.git",
        "https://github.com/shadow-robot/autopic.git",
        "https://github.com/shadow-robot/fh_common.git"
]

def get_repository_branches(repository_url) {

    def result = [:]
    def branches = [:]
    def pull_requests = [:]
    def head_uid = null

    def branch_prefix = "refs/heads/"
    def pull_request_prefix = "refs/pull/"

    def credentials = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
            com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials.class,
            Jenkins.instance, null, null)

    def authenticated_url = repository_url
    if (credentials.size() > 0) {
        def authentication = credentials[0].username + ":" + credentials[0].password
        authenticated_url = repository_url.replace("https://github.com", "https://${authentication}@github.com")
    }

    def command_result = ("git ls-remote ${authenticated_url}").execute()
    command_result.text.readLines().each {
        entry = it.split()
        if (2 == entry.size()) {
            key = entry[1]
            value = entry[0]
            if (key.equals("HEAD")) {
                head_uid = value
            }
            else if (key.startsWith(branch_prefix)) {
                branches[key.substring(branch_prefix.length())] = value
            }
            else if (key.startsWith(pull_request_prefix)) {
                pull_requests[key.substring(pull_request_prefix.length())] = value
            }
        }
    }

    def head_branch_name_entry = branches.find { entry -> entry.value.equals(head_uid) }
    if (null != head_branch_name_entry) {
        result[head_branch_name_entry.key] = "HEAD"
        pull_requests.each { key, value ->
            def pull_request_branch = branches.find { entry -> entry.value.equals(value) }
            if (null != pull_request_branch) {
                def last_slash_index = key.lastIndexOf("/")
                result[pull_request_branch.key] = key.substring(0, last_slash_index)
            }
        }
    }

    return result
}

def process_repository(repository_url, template_job_name) {

	def live_jobs_list = []
	def job_name_prefix = "auto_"

	def repo_name = null
    def github_url = null
    def repo_branches = get_repository_branches(repository_url)
    if (repository_url.endsWith(".git")) {
        github_url = repository_url.substring(0, repository_url.length() - ".git".length())
        index_of_last_slash = github_url.lastIndexOf("/")
        repo_name = github_url.substring(index_of_last_slash + 1)
    }
    else {
        github_url = repository_url
        def url_parts = repository_url.split("/")
        repo_name = url_parts.reverse().find { (null != it)  && (it.lenght() > 0) }
    }

	repo_branches.each { branch_name, type ->

        if (null == type) return

		def repo_branch_job_name = job_name_prefix + repo_name + "_" + branch_name + "_" +
                template_job_name.replace("template_", "")
		def repo_branch_job = Jenkins.instance.projects.find { it.name.equals(repo_branch_job_name) }

		if (null == repo_branch_job) {
			def template = Jenkins.instance.getItem(template_job_name)
			repo_branch_job = Jenkins.instance.copy(template, repo_branch_job_name)
			repo_branch_job.description = "Job for " + repo_name + " branch " + branch_name + " based on template " +
					template_job_name
			repo_branch_job.disabled = false
			def property = repo_branch_job.properties.find {
				it.key.getClass().getName().startsWith("com.coravy.hudson.plugins.github.GithubProjectProperty") }
			property.value.projectUrl = github_url
            repo_branch_job.scm.browser = new GithubWeb(github_url)
			repo_branch_job.scm.userRemoteConfigs[0].url = repository_url
            repo_branch_job.scm.userRemoteConfigs[0].refspec = "+refs/pull/*:refs/remotes/origin/pr/*"
            if ("HEAD".equals(type)) {
                repo_branch_job.scm.branches[0].name = "**/pr/*/merge"
            }
            else {
                repo_branch_job.scm.branches[0].name = "**/pr/${type}/head"
            }
            repo_branch_job.save()

			def job_xml_file = repo_branch_job.getConfigFile();
			def file = job_xml_file.getFile();
			repo_branch_job.updateByXml(new StreamSource(new FileInputStream(file)));
			repo_branch_job.save()
		}
		live_jobs_list.push(repo_branch_job.name)

		// Debug only or separate option !!!
		Jenkins.instance.getView("Job Manager").add(repo_branch_job)
	}

	return live_jobs_list
}

def jobs_to_leave = []
repositories.each {
	def live_jobs = process_repository(it, "template_unit_tests_and_code_coverage")
	jobs_to_leave.addAll(live_jobs)
}

def all_auto_jobs = Jenkins.instance.projects.findAll { it.name.startsWith("auto_") }
def jobs_to_delete = all_auto_jobs.findAll { !jobs_to_leave.any { name -> name == it.name } }
jobs_to_delete.each { it.delete() }
