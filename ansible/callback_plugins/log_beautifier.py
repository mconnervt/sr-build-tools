class CallbackModule(object):
    FIELDS = ['cmd', 'command', 'start', 'end', 'delta', 'msg', 'stdout',
              'stderr']

    def nice_log(self, res, msg_color='green'):
        if type(res) == type(dict()):
            for field in self.FIELDS:
                if field in res.keys():
                    msg = '\n{0}:\n{1}'.format(
                        field, res[field].encode('utf-8'))
                    self._display(msg, color=msg_color)

    def __init__(self, display):
        self._display = display

    def on_any(self, *args, **kwargs):
        pass

    def runner_on_failed(self, host, res, ignore_errors=False):
        if ignore_errors:
            self.nice_log(res, msg_color='yellow')
        else:
            self.nice_log(res, msg_color='red')

    def runner_on_ok(self, host, res):
        self.nice_log(res, msg_color='green')

    def runner_on_error(self, host, msg):
        pass

    def runner_on_skipped(self, host, item=None):
        pass

    def runner_on_unreachable(self, host, res):
        self.nice_log(res, msg_color='yellow')

    def runner_on_no_hosts(self):
        pass

    def runner_on_async_poll(self, host, res, jid, clock):
        self.nice_log(res, msg_color='green')

    def runner_on_async_ok(self, host, res, jid):
        self.nice_log(res, msg_color='green')

    def runner_on_async_failed(self, host, res, jid):
        self.nice_log(res, msg_color='red')

    def playbook_on_start(self):
        pass

    def playbook_on_notify(self, host, handler):
        pass

    def playbook_on_no_hosts_matched(self):
        pass

    def playbook_on_no_hosts_remaining(self):
        pass

    def playbook_on_task_start(self, name, is_conditional):
        pass

    def playbook_on_vars_prompt(self, varname, private=True, prompt=None,
                                encrypt=None, confirm=False,
                                salt_size=None, salt=None, default=None):
        pass

    def playbook_on_setup(self):
        pass

    def playbook_on_import_for_host(self, host, imported_file):
        pass

    def playbook_on_not_import_for_host(self, host, missing_file):
        pass

    def playbook_on_play_start(self, pattern):
        pass

    def playbook_on_stats(self, stats):
        pass
