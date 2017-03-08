package com.dscientia.global;

import android.os.Environment;

/**
 * Created by Noel_Cat on 2016/12/26.
 */

public class Constant {
    public static final String SERVER_ADDRESS = "http://172.18.71.35:8080";
    public static final String LOGIN_API = SERVER_ADDRESS + "/api/login";
    public static final String REGISTER_API = SERVER_ADDRESS + "/api/register";
    public static final String UPDATE_USER_API = SERVER_ADDRESS + "/api/user/update";
    public static final String UPDATE_USER_LOGO_API = SERVER_ADDRESS + "/api/user/upload";
    public static final String ADD_NOTEBOOK_API = SERVER_ADDRESS + "/api/user/addnotebook";
    public static final String UPDATE_NOTEBOOK_API = SERVER_ADDRESS + "/api/notebook/update";
    public static final String UPDATE_NOTEBOOK_COVER_API = SERVER_ADDRESS + "/api/notebook/upload";
    public static final String ADD_NOTE_API = SERVER_ADDRESS + "/api/notebook/addnote";
    public static final String UPDATE_NOTE_API = SERVER_ADDRESS + "/api/note/update";
    public static final String DATABASE_NAME = "Dscientia.db";
    public static final String NOTEBOOK_TABLE_NAME = "NoteBooks";
    public static final String NOTE_TABLE_NAME = "Notes";
    public static final String APP_PATH = Environment.getExternalStorageDirectory() + "/Dscientia/";
    public static final String FILE_NAME = "config";
    public static final String NOTE_LIST_FILE_NAME = "enote_note_name_list.txt";

    public static final String GET_SOME_ACTS_BY_TYPE = SERVER_ADDRESS + "/api/activity/get_some_acts_by_type";
    public static final String GET_MORE_ACTS_BY_TYPE = SERVER_ADDRESS + "/api/activity/get_more_acts_by_type";
    public static final String GET_RECOMMEND__ACTS = SERVER_ADDRESS + "/api/activity/get_recommend_acts";
    public static final String GET_ALL_NOTEBOOKS = SERVER_ADDRESS + "/api/notebook/getinfo";
    public static final String GET_NOTES = SERVER_ADDRESS + "/api/note/getinfo";
    public static final String GET_ACT_BY_SCAN = SERVER_ADDRESS + "/api/activity/get_act_by_url";

}
