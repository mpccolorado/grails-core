import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.codehaus.groovy.grails.plugins.GrailsPluginInfo

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsPlugins")
target(copyFilesFromSourceToDest: "Copy files from source to dest") {
    if (srcdir?.isDirectory()) {
        event "StatusUpdate", ["Copying templates from $userInterfacePluginDir"]

        ant.copy(todir: destdir, overwrite: true, failonerror: false) {
            fileset dir: srcdir
        }
    } else {
        event "StatusError", ["Unable to copy files as plugin source dir is missing"]
    }
}

target(getPluginInfo: "get the plugin info of pluginName") {
    if(!plugin)
        return null

    def pluginInfos = pluginSettings.getPluginInfos()
    for (GrailsPluginInfo info in pluginInfos) {
        if(plugin == info.name){
            return info
        }
    }
    return null
}