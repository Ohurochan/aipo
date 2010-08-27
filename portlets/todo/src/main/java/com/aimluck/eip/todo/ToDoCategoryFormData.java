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
package com.aimluck.eip.todo;

import java.util.Calendar;
import java.util.List;

import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;

import com.aimluck.commons.field.ALStringField;
import com.aimluck.eip.cayenne.om.portlet.EipTTodoCategory;
import com.aimluck.eip.common.ALAbstractFormData;
import com.aimluck.eip.common.ALDBErrorException;
import com.aimluck.eip.common.ALEipConstants;
import com.aimluck.eip.common.ALPageNotFoundException;
import com.aimluck.eip.modules.actions.common.ALAction;
import com.aimluck.eip.orm.Database;
import com.aimluck.eip.orm.query.SelectQuery;
import com.aimluck.eip.services.accessctl.ALAccessControlConstants;
import com.aimluck.eip.services.eventlog.ALEventlogConstants;
import com.aimluck.eip.services.eventlog.ALEventlogFactoryService;
import com.aimluck.eip.todo.util.ToDoUtils;
import com.aimluck.eip.util.ALEipUtils;

/**
 * ToDoカテゴリのフォームデータを管理するクラスです。 <BR>
 * 
 */
public class ToDoCategoryFormData extends ALAbstractFormData {

  /** logger */
  private static final JetspeedLogger logger = JetspeedLogFactoryService
    .getLogger(ToDoCategoryFormData.class.getName());

  /** カテゴリ名 */
  private ALStringField category_name;

  /** メモ */
  private ALStringField note;

  private Integer category_id;

  /** ログインユーザーのID * */
  private int user_id;

  /**
   * 
   * @param action
   * @param rundata
   * @param context
   * 
   * 
   */
  @Override
  public void init(ALAction action, RunData rundata, Context context)
      throws ALPageNotFoundException, ALDBErrorException {
    super.init(action, rundata, context);

    String categoryid =
      ALEipUtils.getTemp(rundata, context, ALEipConstants.ENTITY_ID);
    if (categoryid != null && Integer.valueOf(categoryid) != null) {
      category_id = Integer.valueOf(categoryid);
    }

    user_id = ALEipUtils.getUserId(rundata);
  }

  /**
   *
   *
   */
  public void initField() {
    // カテゴリ名
    category_name = new ALStringField();
    category_name.setFieldName("カテゴリ名");
    category_name.setTrim(true);
    // メモ
    note = new ALStringField();
    note.setFieldName("メモ");
    note.setTrim(true);
  }

  /**
   * ToDoカテゴリの各フィールドに対する制約条件を設定します。 <BR>
   * 
   * 
   */
  @Override
  protected void setValidator() {
    // カテゴリ名必須項目
    category_name.setNotNull(true);
    // カテゴリ名文字数制限
    category_name.limitMaxLength(50);
    // メモ文字数制限
    note.limitMaxLength(1000);
  }

  /**
   * ToDoカテゴリのフォームに入力されたデータの妥当性検証を行います。 <BR>
   * 
   * @param msgList
   * @return
   * 
   */
  @Override
  protected boolean validate(List<String> msgList) {
    try {
      SelectQuery<EipTTodoCategory> query =
        Database.query(EipTTodoCategory.class);

      Expression exp =
        ExpressionFactory.matchExp(
          EipTTodoCategory.CATEGORY_NAME_PROPERTY,
          category_name.getValue());

      Expression exp2 =
        ExpressionFactory.matchExp(EipTTodoCategory.USER_ID_PROPERTY, Integer
          .valueOf(0));

      Expression exp3 =
        ExpressionFactory.matchExp(EipTTodoCategory.USER_ID_PROPERTY, Integer
          .valueOf(this.user_id));

      if (ALEipConstants.MODE_UPDATE.equals(getMode())) {
        Expression exp4 =
          ExpressionFactory.noMatchDbExp(
            EipTTodoCategory.CATEGORY_ID_PK_COLUMN,
            category_id);
        // exp AND (exp4 AND (exp3 OR exp2))
        query.setQualifier(exp4.andExp(exp.andExp(exp3.orExp(exp2))));
      } else {
        // exp1 AND ( exp2 OR exp3 )
        query.setQualifier(exp.andExp(exp2.orExp(exp3)));
      }

      if (query.fetchList().size() != 0) {
        msgList.add("カテゴリ名『 <span class='em'>"
          + category_name.toString()
          + "</span> 』は既に登録されています。");
      }
    } catch (Exception ex) {
      logger.error("Exception", ex);
      return false;
    }

    // カテゴリ名
    category_name.validate(msgList);
    // メモ
    note.validate(msgList);
    return (msgList.size() == 0);
  }

  /**
   * ToDoカテゴリをデータベースから読み出します。 <BR>
   * 
   * @param rundata
   * @param context
   * @param msgList
   * @return
   */
  @Override
  protected boolean loadFormData(RunData rundata, Context context,
      List<String> msgList) {
    try {
      // オブジェクトモデルを取得
      EipTTodoCategory category =
        ToDoUtils.getEipTTodoCategory(rundata, context);
      if (category == null) {
        return false;
      }
      // カテゴリ名
      category_name.setValue(category.getCategoryName());
      // メモ
      note.setValue(category.getNote());
    } catch (Exception ex) {
      logger.error("Exception", ex);
      return false;
    }
    return true;
  }

  /**
   * ToDoカテゴリをデータベースに格納します。 <BR>
   * 
   * @param rundata
   * @param context
   * @param msgList
   * @return
   */
  @Override
  protected boolean insertFormData(RunData rundata, Context context,
      List<String> msgList) {
    try {
      EipTTodoCategory category = Database.create(EipTTodoCategory.class);
      category.setCategoryName(category_name.getValue());
      category.setNote(note.getValue());
      category.setUserId(Integer.valueOf(ALEipUtils.getUserId(rundata)));
      category.setCreateDate(Calendar.getInstance().getTime());
      category.setUpdateDate(Calendar.getInstance().getTime());
      Database.commit();

      // イベントログに保存
      ALEventlogFactoryService.getInstance().getEventlogHandler().log(
        category.getCategoryId(),
        ALEventlogConstants.PORTLET_TYPE_TODO_CATEGORY,
        category_name.getValue());

    } catch (Throwable t) {
      Database.rollback();
      logger.error(t);
      return false;
    }

    return true;
  }

  /**
   * データベースに格納されているToDoカテゴリを更新します。 <BR>
   * 
   * @param rundata
   * @param context
   * @param msgList
   * @return
   */
  @Override
  protected boolean updateFormData(RunData rundata, Context context,
      List<String> msgList) {
    try {
      // オブジェクトモデルを取得
      EipTTodoCategory category =
        ToDoUtils.getEipTTodoCategory(rundata, context);
      if (category == null) {
        return false;
      }
      // カテゴリ名
      category.setCategoryName(category_name.getValue());
      // メモ
      category.setNote(note.getValue());
      // ユーザーID
      category.setUserId(Integer.valueOf(ALEipUtils.getUserId(rundata)));
      // 更新日
      category.setUpdateDate(Calendar.getInstance().getTime());

      // Todoカテゴリを更新
      Database.commit();

      // イベントログに保存
      ALEventlogFactoryService.getInstance().getEventlogHandler().log(
        category.getCategoryId(),
        ALEventlogConstants.PORTLET_TYPE_TODO_CATEGORY,
        category_name.getValue());

    } catch (Throwable t) {
      Database.rollback();
      logger.error(t);
      return false;
    }
    return true;
  }

  /**
   * ToDoカテゴリを削除します。 <BR>
   * 
   * @param rundata
   * @param context
   * @param msgList
   * @return
   */
  @Override
  protected boolean deleteFormData(RunData rundata, Context context,
      List<String> msgList) {
    try {
      // オブジェクトモデルを取得
      EipTTodoCategory category =
        ToDoUtils.getEipTTodoCategory(rundata, context);
      if (category == null) {
        return false;
      }

      // entityIdを取得
      Integer entityId = category.getCategoryId();
      // カテゴリ名を取得
      String categoryName = category.getCategoryName();

      // Todoカテゴリを削除
      Database.delete(category);
      Database.commit();

      // ログに保存
      ALEventlogFactoryService.getInstance().getEventlogHandler().log(
        entityId,
        ALEventlogConstants.PORTLET_TYPE_TODO_CATEGORY,
        categoryName);

      // 一覧表示画面のフィルタに設定されているカテゴリのセッション情報を削除
      String filtername =
        ToDoSelectData.class.getName() + ALEipConstants.LIST_FILTER;
      ALEipUtils.removeTemp(rundata, context, filtername);

    } catch (Throwable t) {
      Database.rollback();
      logger.error(t);
      return false;
    }
    return true;
  }

  /**
   * カテゴリ名を取得します。 <BR>
   * 
   * @return
   */
  public ALStringField getCategoryName() {
    return category_name;
  }

  /**
   * メモを取得します。 <BR>
   * 
   * @return
   */
  public ALStringField getNote() {
    return note;
  }

  /**
   * アクセス権限チェック用メソッド。<br />
   * アクセス権限の機能名を返します。
   * 
   * @return
   */
  @Override
  public String getAclPortletFeature() {
    return ALAccessControlConstants.POERTLET_FEATURE_TODO_CATEGORY_SELF;
  }
}
