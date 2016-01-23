import jenkins.model.Jenkins

def all_auto_jobs = Jenkins.instance.projects.findAll { it.name.startsWith("auto_") }
all_auto_jobs.each { it.delete() }
