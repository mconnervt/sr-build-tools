import jenkins.model.Jenkins
import java.io.InputStream;
import java.io.FileInputStream
import java.io.File;
import javax.xml.transform.stream.StreamSource
import hudson.plugins.git.browser.GithubWeb


//def get_repository_branches(repository_url) {
//
//	def branches = [:]
//	def hash_codes = [:]
//	def command_result = ("git ls-remote ${repository_url}").execute()
//	command_result.eachLine {
//		entry = it.split()
//		branches.
//	}
//
//}

def process_repository(repository_url) {

	def live_jobs_list = []

	def job_name_prefix = "auto_"
	def template_job_name = "template_unit_tests_and_code_coverage"

	// TODO: add code to extract repo name from URL
	def repo_name = "build-servers-check"

	def command_result = ("git ls-remote -h ${repository_url}").execute()
	def repo_branches = command_result.text.readLines().collect {  it.replaceAll("[a-z0-9]*\\trefs\\/heads\\/", "") }

	def repo_jobs_prefix = job_name_prefix + repo_name + "_"
	chosen_jobs = Jenkins.instance.projects.findAll { it.name.startsWith(repo_jobs_prefix) }

	repo_branches.each
	{
        def github_url = "https://github.com/shadow-robot/build-servers-check" + "/tree/" + java.net.URLEncoder.encode(it)
		def repo_branch_job_prefix = repo_jobs_prefix + it + "_"
		repo_branch_jobs = chosen_jobs.findAll { it.name.startsWith(repo_branch_job_prefix) }

		if (repo_branch_jobs.size() > 1)
		{
			delete_count = repo_branch_jobs.size() - 1
			delete_count.times
			{
				// TODO: Implement job duplication removal
			}
		}

		def repo_branch_job
		if (0 == repo_branch_jobs.size())
		{
			def template = Jenkins.instance.getItem(template_job_name)
			repo_branch_job = Jenkins.instance.copy(template, repo_branch_job_prefix + template_job_name.replace("template_", ""))
			repo_branch_job.description = "Job for " + repo_name + " branch " + it + " based on template " + template_job_name
			repo_branch_job.disabled = false
			def property = repo_branch_job.properties.find { it.key.getClass().getName().startsWith("com.coravy.hudson.plugins.github.GithubProjectProperty") }
			property.value.projectUrl = github_url
            repo_branch_job.scm.browser = new GithubWeb(github_url)
			repo_branch_job.scm.userRemoteConfigs[0].url = repository_url
			repo_branch_job.scm.branches[0].name = "**/" + it
            def trigger = repo_branch_job.triggers.find { it.key.getClass().getName().startsWith("org.jenkinsci.plugins.ghprb.GhprbTrigger") }
            trigger.value.whiteListTargetBranches[0].branch = it
            repo_branch_job.save()

			def job_xml_file = repo_branch_job.getConfigFile();
			def file = job_xml_file.getFile();
			repo_branch_job.updateByXml(new StreamSource(new FileInputStream(file)));
			repo_branch_job.save()
		}
		else
		{
			repo_branch_job = repo_branch_jobs[0]
		}
		live_jobs_list.push(repo_branch_job.name)

		// Debug only or separate option !!!
		Jenkins.instance.getView("Job Manager").add(repo_branch_job)
	}

	return live_jobs_list
}

//def repositories = ["https://github.com/shadow-robot/fh_common.git"]
def repositories = [
	"https://github.com/shadow-robot/build-servers-check.git"
]

def jobs_to_leave = []
repositories.each
{
	def live_jobs = process_repository(it)
	jobs_to_leave.addAll(live_jobs)
}

def all_auto_jobs = Jenkins.instance.projects.findAll { it.name.startsWith("auto_") }
def jobs_to_delete = all_auto_jobs.findAll { !jobs_to_leave.any { name -> name == it.name } }
jobs_to_delete.each { it.delete() }
