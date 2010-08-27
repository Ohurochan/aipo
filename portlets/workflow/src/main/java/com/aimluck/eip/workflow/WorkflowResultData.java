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
package com.aimluck.eip.workflow;

import java.util.ArrayList;
import java.util.List;

import com.aimluck.commons.field.ALNumberField;
import com.aimluck.commons.field.ALStringField;
import com.aimluck.eip.common.ALData;
import com.aimluck.eip.fileupload.beans.FileuploadBean;
import com.aimluck.eip.util.ALCommonUtils;
import com.aimluck.eip.workflow.util.WorkflowUtils;

/**
 * ワークフローのResultDataです。 <BR>
 * 
 */
public class WorkflowResultData implements ALData {

  /** Request ID */
  protected ALNumberField request_id;

  /** Request名 */
  protected ALStringField request_name;

  /** カテゴリID */
  protected ALNumberField category_id;

  /** カテゴリ名 */
  protected ALStringField category_name;

  /** 重要度 */
  protected ALNumberField priority;

  /** 重要度画像名 */
  protected ALStringField priority_image;

  /** 重要度（文字列） */
  protected ALStringField priority_string;

  /** 進捗（文字列） */
  protected ALStringField state_string;

  /** 状態 */
  protected ALStringField progress;

  /** 金額 */
  protected ALNumberField price;

  /** 登録日 */
  protected ALStringField create_date;

  /** 最終閲覧者名 */
  protected ALStringField last_update_user;

  /** 添付ファイルリスト */
  private List<FileuploadBean> attachmentFileList = null;

  /**
   * 
   * 
   */
  public void initField() {
    request_id = new ALNumberField();
    request_name = new ALStringField();
    category_id = new ALNumberField();
    category_name = new ALStringField();
    priority = new ALNumberField();
    priority_image = new ALStringField();
    priority_string = new ALStringField();
    state_string = new ALStringField();
    progress = new ALStringField();
    price = new ALNumberField();
    create_date = new ALStringField();
    last_update_user = new ALStringField();
    attachmentFileList = new ArrayList<FileuploadBean>();
  }

  /**
   * @return
   */
  public ALNumberField getRequestId() {
    return request_id;
  }

  /**
   * @return
   */
  public String getRequestName() {
    return ALCommonUtils.replaceToAutoCR(request_name.toString());
  }

  /**
   * @return
   */
  public ALNumberField getCategoryId() {
    return category_id;
  }

  /**
   * @return
   */
  public String getCategoryName() {
    return ALCommonUtils.replaceToAutoCR(category_name.toString());
  }

  /**
   * @return
   */
  public ALStringField getStateString() {
    return state_string;
  }

  /**
   * @return
   */
  public ALStringField getPriorityString() {
    return priority_string;
  }

  /**
   * @return
   */
  public ALStringField getPriorityImage() {
    return priority_image;
  }

  /**
   * @return
   */
  public ALStringField getProgress() {
    return progress;
  }

  /**
   * @return
   */
  public ALNumberField getPrice() {
    return price;
  }

  public String getPriceStr() {
    return WorkflowUtils.translateMoneyStr(price.toString());
  }

  /**
   * @return
   */
  public ALStringField getLastUpdateUser() {
    return last_update_user;
  }

  /**
   * @param i
   */
  public void setRequestId(long i) {
    request_id.setValue(i);
  }

  /**
   * @param string
   */
  public void setRequestName(String string) {
    request_name.setValue(string);
  }

  /**
   * @param i
   */
  public void setCategoryId(long i) {
    category_id.setValue(i);
  }

  /**
   * @param i
   */
  public void setCategoryName(String string) {
    category_name.setValue(string);
  }

  /**
   * @param i
   */
  public void setPriority(int i) {
    priority.setValue(i);
  }

  /**
   * @param string
   */
  public void setPriorityImage(String string) {
    priority_image.setValue(string);
  }

  /**
   * @param string
   */
  public void setPriorityString(String string) {
    priority_string.setValue(string);
  }

  /**
   * @param string
   */
  public void setStateString(String string) {
    state_string.setValue(string);
  }

  /**
   * @param string
   */
  public void setProgress(String string) {
    progress.setValue(string);
  }

  /**
   * @param string
   */
  public void setPrice(long i) {
    price.setValue(i);
  }

  /**
   * @return
   */
  public ALStringField getCreateDate() {
    return create_date;
  }

  /**
   * @param string
   */
  public void setCreateDate(String string) {
    create_date.setValue(string);
  }

  /**
   * @param string
   */
  public void setLastUpdateUser(String string) {
    last_update_user.setValue(string);
  }

  /**
   * @return list
   */
  public List<FileuploadBean> getAttachmentFileList() {
    return attachmentFileList;
  }

  /**
   * @param list
   */
  public void setAttachmentFiles(List<FileuploadBean> list) {
    attachmentFileList = list;
  }

}
