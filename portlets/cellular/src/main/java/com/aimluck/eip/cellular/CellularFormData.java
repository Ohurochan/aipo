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
package com.aimluck.eip.cellular;

import java.util.ArrayList;
import java.util.List;

import org.apache.jetspeed.om.security.UserNamePrincipal;
import org.apache.jetspeed.services.JetspeedUserManagement;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.jetspeed.services.resources.JetspeedResources;
import org.apache.jetspeed.services.rundata.JetspeedRunData;
import org.apache.jetspeed.services.security.JetspeedSecurityException;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;

import com.aimluck.commons.field.ALStringField;
import com.aimluck.eip.cellular.util.CellularUtils;
import com.aimluck.eip.common.ALAbstractFormData;
import com.aimluck.eip.common.ALBaseUser;
import com.aimluck.eip.common.ALDBErrorException;
import com.aimluck.eip.common.ALEipUser;
import com.aimluck.eip.common.ALPageNotFoundException;
import com.aimluck.eip.mail.util.ALEipUserAddr;
import com.aimluck.eip.mail.util.ALMailUtils;
import com.aimluck.eip.modules.actions.common.ALAction;
import com.aimluck.eip.orm.DatabaseOrmService;
import com.aimluck.eip.util.ALEipUtils;

/**
 * フォームデータを管理するクラス
 * 
 */
public class CellularFormData extends ALAbstractFormData {

  /** logger */
  private static final JetspeedLogger logger = JetspeedLogFactoryService
    .getLogger(CellularFormData.class.getName());

  /** 簡易アクセス用 URL */
  private ALStringField cellular_url;

  /** 簡易ログイン設定の有効／無効 */
  private boolean enableEasyLogin;

  /** ログインユーザー */
  private ALBaseUser baseUser;

  private String url;

  private String org_id;

  @Override
  public void init(ALAction action, RunData rundata, Context context)
      throws ALPageNotFoundException, ALDBErrorException {
    super.init(action, rundata, context);

    enableEasyLogin = false;

    org_id = DatabaseOrmService.getInstance().getOrgId(rundata);

    JetspeedRunData jdata = (JetspeedRunData) rundata;
    try {
      // 最新のユーザ情報を取得する．
      baseUser =
        (ALBaseUser) JetspeedUserManagement.getUser(new UserNamePrincipal(jdata
          .getJetspeedUser()
          .getUserName()));
    } catch (JetspeedSecurityException e) {
      e.printStackTrace();
      baseUser = (ALBaseUser) rundata.getUser();
    }

    url = CellularUtils.getCellularUrl(rundata, context);
  }

  /**
   * 各フィールドを初期化する．
   * 
   * 
   */
  public void initField() {
    cellular_url = new ALStringField();
    cellular_url.setFieldName(DatabaseOrmService.getInstance().getAlias()
      + "サイトのアドレス");
    cellular_url.setTrim(true);
  }

  /**
   * 各フィールドに対する制約条件を設定します。 <BR>
   * 
   * 
   */
  @Override
  protected void setValidator() {
  }

  /**
   * フォームに入力されたデータの妥当性を検証する．
   * 
   * @param msgList
   * @return
   * 
   */
  @Override
  protected boolean validate(List<String> msgList) {

    return (msgList.size() == 0);
  }

  /**
   * 
   * @param rundata
   * @param context
   * @param msgList
   * @return
   */
  @Override
  protected boolean loadFormData(RunData rundata, Context context,
      List<String> msgList) {
    String uid = baseUser.getCelluarUId();

    enableEasyLogin = (uid != null && uid.length() > 0);

    cellular_url.setValue(url);

    return true;
  }

  /**
   * 
   * @param rundata
   * @param context
   * @param msgList
   * @return
   */
  @Override
  protected boolean insertFormData(RunData rundata, Context context,
      List<String> msgList) {
    boolean res = false;

    cellular_url.setValue(url);

    try {
      if (url == null || url.length() == 0) {
        msgList.add(DatabaseOrmService.getInstance().getAlias()
          + "サイトのアドレスが未設定のため、メールを送信できませんでした。"
          + DatabaseOrmService.getInstance().getAlias()
          + "の管理担当者にお問い合わせください。"
          + DatabaseOrmService.getInstance().getAlias()
          + "サイトのアドレスは、管理画面で設定できます。");
        return false;
      }

      ALEipUser user = ALEipUtils.getALEipUser(rundata);

      List<ALEipUser> memberList = new ArrayList<ALEipUser>();
      memberList.add(ALEipUtils.getALEipUser(rundata));
      List<ALEipUserAddr> destMemberList =
        ALMailUtils.getALEipUserAddrs(
          memberList,
          ALEipUtils.getUserId(rundata),
          true);

      ALEipUserAddr addr = null;
      addr = destMemberList.get(0);
      if (addr.getCellMailAddr() == null || addr.getCellMailAddr().equals("")) {
        msgList
          .add("ユーザーの携帯電話のメールアドレスが設定されていないため、メールを送信できませんでした。携帯電話のメールアドレスは、個人設定のユーザー情報から変更できます。");
        return false;
      }

      String subject =
        "[" + DatabaseOrmService.getInstance().getAlias() + "] 携帯電話用ログインURL";
      String body = createMsgForCellPhone(url);

      res =
        ALMailUtils.sendMailDelegate(
          org_id,
          (int) user.getUserId().getValue(),
          destMemberList,
          null,
          subject,
          null,
          body,
          ALMailUtils.VALUE_MSGTYPE_DEST_CELLULAR,
          msgList);
      if (!res) {
        msgList.add("メールアカウントが正しく設定されていないため、メールを送信できませんでした。"
          + DatabaseOrmService.getInstance().getAlias()
          + "の管理担当者にお問い合わせください。メールアカウントは、管理画面で設定できます。");
      }

    } catch (Exception ex) {
      logger.error("Exception", ex);
      return false;
    }
    return res;
  }

  /**
   * 
   * @param rundata
   * @param context
   * @param msgList
   * @return
   */
  @Override
  protected boolean updateFormData(RunData rundata, Context context,
      List<String> msgList) {
    return false;
  }

  /**
   * 
   * @param rundata
   * @param context
   * @param msgList
   * @return
   */
  @Override
  protected boolean deleteFormData(RunData rundata, Context context,
      List<String> msgList) {
    return false;
  }

  /**
   * 携帯電話へ送信するメールの内容を作成する．
   * 
   * @return
   */
  private String createMsgForCellPhone(String url) {
    String CR = System.getProperty("line.separator");
    StringBuffer body =
      new StringBuffer(JetspeedResources.getString("aipo.alias"));
    body.append("からのお知らせです。以下に記載されたURLから");
    body.append(DatabaseOrmService.getInstance().getAlias());
    body.append("にログインしてください。");
    body.append("ログインするときには通常使用しているログインパスワードを入力してください。").append(CR).append(CR);

    body.append(url).append(CR);

    return body.toString();
  }

  public ALStringField getCellularUrl() {
    return cellular_url;
  }

  public boolean enableEasyLogin() {
    return enableEasyLogin;
  }
}
