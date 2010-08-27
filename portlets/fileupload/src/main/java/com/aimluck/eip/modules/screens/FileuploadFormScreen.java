/*
 * Aipo is a groupware program developed by Aimluck,Inc.
 * Copyright (C) 2004-2008 Aimluck,Inc.
 * http://aipostyle.com/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aimluck.eip.modules.screens;

import javax.servlet.http.HttpServletResponse;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;

import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.fileupload.FileuploadFormData;
import com.aimluck.eip.fileupload.util.FileuploadUtils;
import com.aimluck.eip.util.ALEipUtils;

/**
 * ファイルのアップロードを処理するクラスです。 <br />
 * 
 */
public class FileuploadFormScreen extends ALVelocityScreen {

  /** logger */
  private static final JetspeedLogger logger = JetspeedLogFactoryService
    .getLogger(FileuploadFormScreen.class.getName());

  /**
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
  @Override
  protected void doOutput(RunData rundata, Context context) throws Exception {

    String mode = rundata.getParameters().getString("mode", "");
    try {
      if (ALEipConstants.MODE_FORM.equals(mode)) {
        doFileupload_form(rundata, context);
      } else if (ALEipConstants.MODE_UPDATE.equals(mode)) {
        doFileupload_update(rundata, context);
      }
    } catch (Exception ex) {
      logger.error("[FileuploadFormScreen] Exception.", ex);
      ALEipUtils.redirectDBError(rundata);
    }
  }

  /**
   * 添付ファイルの入力フォームを開く．
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
  protected void doFileupload_form(RunData rundata, Context context)
      throws Exception {
    context.put("data", rundata);
    context.put("js_peid", rundata.getParameters().getString("js_peid", ""));
    context.put("msize", rundata.getParameters().getString(
      FileuploadUtils.KEY_MAX_SIZE,
      "0"));
    context.put("nsize", rundata.getParameters().getString(
      FileuploadUtils.KEY_NOW_SIZE,
      "0"));

    HttpServletResponse response = rundata.getResponse();
    response
      .setContentType("text/html; " + ALEipConstants.DEF_CONTENT_ENCODING);

    FileuploadFormData formData = new FileuploadFormData();
    formData.initField();
    formData.doViewForm(this, rundata, context);

    String layout_template = "layouts/html/ja/fileupload.vm";
    setTemplate(rundata, context, layout_template);
  }

  /**
   * 添付ファイルのアップロードを受け付ける．
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
  protected void doFileupload_update(RunData rundata, Context context)
      throws Exception {

    context.put("data", rundata);
    context.put("js_peid", rundata.getParameters().getString("js_peid", ""));

    HttpServletResponse response = rundata.getResponse();
    response
      .setContentType("text/html; " + ALEipConstants.DEF_CONTENT_ENCODING);

    FileuploadFormData formData = new FileuploadFormData();
    formData.initField();
    if (formData.doUpdate(this, rundata, context)) {
      context.put("receiveFile", "true");
    }
    String layout_template = "layouts/html/ja/fileupload.vm";
    setTemplate(rundata, context, layout_template);

  }

}
