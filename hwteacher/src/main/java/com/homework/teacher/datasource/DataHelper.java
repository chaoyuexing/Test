package com.homework.teacher.datasource;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.homework.teacher.data.ClassRoom;

public class DataHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "vsichu.db";
	private static final int DATABASE_VERSION = 1;
	private static DataHelper instance;
	private Dao<ClassRoom, Integer> classroomData = null;

	public DataHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, ClassRoom.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized DataHelper getHelper(Context context) {
		if (instance == null) {
			synchronized (DataHelper.class) {
				if (instance == null)
					instance = new DataHelper(context);
			}
		}

		return instance;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int arg2, int arg3) {
		try {
			TableUtils.dropTable(connectionSource, ClassRoom.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		super.close();
	}

	public Dao<ClassRoom, Integer> getClassRoomData() throws SQLException {
		if (classroomData == null)
			classroomData = getDao(ClassRoom.class);
		return classroomData;
	}

}
