package com.thingworx.extension.custom.ftp;

import com.thingworx.entities.utils.ThingUtilities;
import com.thingworx.logging.LogUtilities;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.resources.Resource;
import com.thingworx.things.repository.FileRepositoryThing;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.slf4j.Logger;

public class FTPResource extends Resource {

  private final static Logger SCRIPT_LOGGER = LogUtilities.getInstance().getScriptLogger(FTPResource.class);
  private static final long serialVersionUID = 1L;

  @ThingworxServiceDefinition(name = "download", description = "", category = "", isAllowOverride = false, aspects = {"isAsync:false"})
  public void download(
          @ThingworxServiceParameter(name = "url", description = "", baseType = "STRING", aspects = {"isRequired:true"}) String url,
          @ThingworxServiceParameter(name = "fileRepository", description = "", baseType = "THINGNAME", aspects = {"isRequired:true", "thingTemplate:FileRepository"}) String fileRepository,
          @ThingworxServiceParameter(name = "path", description = "", baseType = "STRING", aspects = {"isRequired:true"}) String path) throws Exception {
    SCRIPT_LOGGER.debug("FTPResource - download -> Start");

    URLConnection urlConnection = new URL(url).openConnection();
    FileRepositoryThing fileRepositoryThing = (FileRepositoryThing) ThingUtilities.findThing(fileRepository);

    try ( InputStream is = urlConnection.getInputStream();  FileOutputStream fos = fileRepositoryThing.openFileForWrite(path, FileRepositoryThing.FileMode.WRITE)) {
      is.transferTo(fos);
    }

    SCRIPT_LOGGER.debug("FTPResource - download -> Stop");
  }
}
