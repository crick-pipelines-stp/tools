from nf_core.components.install import ComponentInstall


class SubworkflowInstall(ComponentInstall):
    def __init__(
        self,
        pipeline_dir,
        force=False,
        prompt=False,
        sha=None,
        remote_url=None,
        branch=None,
        no_pull=False,
        installed_by=False,
    ):
        super().__init__(
            pipeline_dir,
            "subworkflows",
            force=force,
            prompt=prompt,
            sha=sha,
            remote_url=remote_url,
            branch=branch,
            no_pull=no_pull,
            installed_by=installed_by,
        )
