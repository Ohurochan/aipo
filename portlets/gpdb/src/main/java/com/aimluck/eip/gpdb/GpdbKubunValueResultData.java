/*
 * Aipo is a groupware program developed by TOWN, Inc.
 * Copyright (C) 2004-2015 TOWN, Inc.
 * http://www.aipo.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aimluck.eip.gpdb;

import com.aimluck.commons.field.ALNumberField;
import com.aimluck.commons.field.ALStringField;
import com.aimluck.eip.common.ALData;
import com.aimluck.eip.util.ALCommonUtils;

/**
 * WebデータベースのResultDataです。 <BR>
 * 
 */
public class GpdbKubunValueResultData implements ALData {

  /** 区分値ID */
  protected ALNumberField gpdb_kubun_value_id;

  /** 区分ID */
  protected ALNumberField gpdb_kubun_id;

  /** 区分名 */
  protected ALStringField gpdb_kubun_name;

  /** 区分値 */
  protected ALStringField gpdb_kubun_value;

  /**
   *
   *
   */
  @Override
  public void initField() {
    gpdb_kubun_value_id = new ALNumberField();
    gpdb_kubun_id = new ALNumberField();
    gpdb_kubun_name = new ALStringField();
    gpdb_kubun_value = new ALStringField();
  }

  /**
   * 区分値IDを取得する
   * 
   * @return 区分値ID
   */
  public ALNumberField getGpdbKubunValueId() {
    return gpdb_kubun_value_id;
  }

  /**
   * 区分値IDを設定する
   * 
   * @param i
   *          区分値ID
   */
  public void setGpdbKubunValueId(long i) {
    gpdb_kubun_value_id.setValue(i);
  }

  /**
   * 区分マスタIDを取得する
   * 
   * @return　区分マスタID
   */
  public ALNumberField getGpdbKubunId() {
    return gpdb_kubun_id;
  }

  /**
   * 区分マスタIDを設定する
   * 
   * @param i
   *          区分マスタID
   */
  public void setGpdbKubunId(long i) {
    gpdb_kubun_id.setValue(i);
  }

  /**
   * 区分名を取得する
   * 
   * @return 区分名
   */
  public ALStringField getGpdbKubunName() {
    return gpdb_kubun_name;
  }

  /**
   * 区分名を取得する(Wbr挿入)
   * 
   * @return 区分名
   */
  public String getWbrGpdbKubunName() {
    return ALCommonUtils.replaceToAutoCR(gpdb_kubun_name.toString());
  }

  /**
   * 区分名を設定する
   * 
   * @param string
   *          区分名
   */
  public void setGpdbKubunName(String string) {
    gpdb_kubun_name.setValue(string);
  }

  /**
   * 区分値を取得する
   * 
   * @return 区分値
   */
  public ALStringField getGpdbKubunValue() {
    return gpdb_kubun_value;
  }

  /**
   * 区分値を取得する(Wbr挿入)
   * 
   * @return 区分値
   */
  public String getWbrGpdbKubunValue() {
    return ALCommonUtils.replaceToAutoCR(gpdb_kubun_value.toString());
  }

  /**
   * 区分値を設定する
   * 
   * @param string
   *          区分値
   */
  public void setGpdbKubunValue(String string) {
    gpdb_kubun_value.setValue(string);
  }
}
