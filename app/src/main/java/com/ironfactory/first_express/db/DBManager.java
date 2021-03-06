package com.ironfactory.first_express.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ironfactory.first_express.entities.OptionEntity;
import com.ironfactory.first_express.entities.ProductEntity;
import com.ironfactory.first_express.entities.RoomEntity;
import com.ironfactory.first_express.entities.PersonEntity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IronFactory on 2016. 1. 12..
 */
public class DBManager extends SQLiteOpenHelper {

    private static final String TAG = "DBManager";
    private static final String PRODUCT_TABLE_NAME = "products";
    private static final String ROOM_SIZE_TABLE_NAME = "roomSizes";
    private static final String PERSON_NUM_TABLE_NAME = "personNums";
    private static final String OPTION_TABLE_NAME = "options";


    private static final String COL_NAME = "names";
    private static final String COL_PRICE = "prices";
    private static final String COL_ROOM_SIZE = "roomSize";
    private static final String COL_NUM = "num";

    private static final int PRODUCT_LENGTH = 14;
    private static final int ROOM_LENGTH = 5;
    private static final int PERSON_LENGTH = 25;
    private static final int OPTION_LENGTH = 7;



    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PRODUCT_TABLE_NAME + " (" + COL_NAME + " TEXT, " + COL_PRICE + "INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ROOM_SIZE_TABLE_NAME + " (" + COL_NAME + " TEXT, " + COL_NUM + " INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PERSON_NUM_TABLE_NAME + " (" + COL_ROOM_SIZE + " INTEGER, " + COL_NAME + " TEXT, " + COL_PRICE + "INTEGER, " + COL_NUM + " INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + OPTION_TABLE_NAME + " (" + COL_NAME + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean checkDB() {
        if (getProduct().size() < PRODUCT_LENGTH)
            return false;
        if (getRoom().size() < ROOM_LENGTH)
            return false;
        if (getPerson().size() < PERSON_LENGTH)
            return false;
        if (getOption().size() < OPTION_LENGTH)
            return false;
        return true;
    }


    public void insertProduct(ProductEntity productEntity) {
        String name = productEntity.getName();
        int price = productEntity.getPrice();

        String command = "INSERT INTO " + PRODUCT_TABLE_NAME + " values('" + name + "', " + price + ");";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(command);
        db.close();
    }

    public void insertRoomSize(RoomEntity roomEntity) {
        String name = roomEntity.getName();
        int num = roomEntity.getNum();

        String command = "INSERT INTO " + ROOM_SIZE_TABLE_NAME + " values('" + name + "', " + num + ");";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(command);
        db.close();
    }

    public void insertPersonNum(PersonEntity personEntity) {
        int roomNum = personEntity.getRoomNum();
        String name = personEntity.getName();
        int price = personEntity.getPrice();
        int num = personEntity.getNum();

        String command = "INSERT INTO " + PERSON_NUM_TABLE_NAME + " values(" + roomNum + ", '" + name + "', " + price + ", " + num + ");";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(command);
        db.close();
    }

    public void insertOption(OptionEntity optionEntity) {
        String name = optionEntity.getName();

        String command = "INSERT INTO " + OPTION_TABLE_NAME + " values('" + name + "');";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(command);
        db.close();
    }


    public ArrayList<ProductEntity> getProduct() {
        String command = "SELECT * FROM " + PRODUCT_TABLE_NAME + ";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(command, null);
        ArrayList<ProductEntity> productEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            int price = cursor.getInt(1);

            ProductEntity productEntity = new ProductEntity(name, price);
            productEntities.add(productEntity);
        }

        return productEntities;
    }


    public ArrayList<RoomEntity> getRoom() {
        String command = "SELECT * FROM " + ROOM_SIZE_TABLE_NAME + ";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(command, null);
        ArrayList<RoomEntity> roomEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            int num = cursor.getInt(1);

            RoomEntity roomEntity = new RoomEntity();
            roomEntity.setName(name);
            roomEntity.setNum(num);
            roomEntities.add(roomEntity);
        }
        return roomEntities;
    }


    public PersonEntity getPerson(int roomNum, int personNum) {
        String command = "SELECT * FROM " + PERSON_NUM_TABLE_NAME + " WHERE " + COL_ROOM_SIZE + " = " + roomNum + " AND " + COL_NUM + " = " + personNum + ";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(command, null);
        PersonEntity personEntity = new PersonEntity();
        cursor.moveToFirst();
        int roomSize = cursor.getInt(0);
        String name = cursor.getString(1);
        int price = cursor.getInt(2);
        int num = cursor.getInt(3);

        personEntity.setRoomNum(roomSize);
        personEntity.setName(name);
        personEntity.setPrice(price);
        personEntity.setNum(num);

        return personEntity;
    }


    public ArrayList<PersonEntity> getPerson() {
        String command = "SELECT * FROM " + PERSON_NUM_TABLE_NAME + ";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(command, null);
        ArrayList<PersonEntity> personEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            int roomSize = cursor.getInt(0);
            String name = cursor.getString(1);
            int price = cursor.getInt(2);
            int num = cursor.getInt(3);

            PersonEntity personEntity = new PersonEntity();
            personEntity.setRoomNum(roomSize);
            personEntity.setName(name);
            personEntity.setPrice(price);
            personEntity.setNum(num);
            personEntities.add(personEntity);
        }
        return personEntities;
    }


    public ArrayList<OptionEntity> getOption() {
        String command = "SELECT * FROM " + OPTION_TABLE_NAME + ";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(command, null);
        ArrayList<OptionEntity> optionEntities = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);

            OptionEntity optionEntity = new OptionEntity();
            optionEntity.setName(name);
            optionEntity.setIsSelected(false);
        }
        return optionEntities;
    }


    public HashMap<String, OptionEntity> getOptionToMap() {
        String command = "SELECT * FROM " + OPTION_TABLE_NAME + ";";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(command, null);
        HashMap<String, OptionEntity> optionHash = new HashMap<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);

            OptionEntity optionEntity = new OptionEntity();
            optionEntity.setName(name);
            optionEntity.setIsSelected(false);

            optionHash.put(name, optionEntity);
        }
        return optionHash;
    }


//    public void updateMember(MemberEntity memberEntity) {
//        String command = "UPDATE " + TABLE_NAME + " set " +
//                COL_GROUP + " = " + memberEntity.getGroup() + ", " +
//                COL_IMG_URI + " = '" + memberEntity.getImage().toString() + "', " +
//                COL_MSG + " = '" + memberEntity.getMessage() + "' " +
//                "WHERE " + COL_ID + " = " + memberEntity.getId() + ";";
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL(command);
//        db.close();
//    }
//
//
    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + PRODUCT_TABLE_NAME + ";");
        db.execSQL("DELETE FROM " + PERSON_NUM_TABLE_NAME + ";");
        db.execSQL("DELETE FROM " + ROOM_SIZE_TABLE_NAME + ";");
        db.execSQL("DELETE FROM " + OPTION_TABLE_NAME + ";");
        db.close();
    }


    public void insertAll() {
        ProductEntity productEntity = new ProductEntity(ProductEntity.WARDROBE, 62400);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.CLOSET, 21300);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.BED, 16200);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.DRESSING_TABLE, 13300);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.DESK, 13100);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.BOOK_CASE, 23800);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.LIVING_ROOM_DRESSING, 11700);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.SOFA, 23500);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.REFRIGERATOR, 21700);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.WASHER, 13400);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.TABLE, 11200);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.DRAWER, 10700);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.CHAIR, 3600);
        insertProduct(productEntity);
        productEntity = new ProductEntity(ProductEntity.POT, 5400);
        insertProduct(productEntity);



        RoomEntity roomEntity = new RoomEntity("10평 미만", 10);
        insertRoomSize(roomEntity);
        roomEntity = new RoomEntity("20평 미만", 20);
        insertRoomSize(roomEntity);
        roomEntity = new RoomEntity("30평 미만", 30);
        insertRoomSize(roomEntity);
        roomEntity = new RoomEntity("40평 미만", 40);
        insertRoomSize(roomEntity);
        roomEntity = new RoomEntity("40평 이상", 50);
        insertRoomSize(roomEntity);


        PersonEntity personEntity = new PersonEntity(1, 255200, "1인", 10);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(2, 312800, "2인", 10);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(3, 326700, "3인", 10);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(4, 503500, "4인", 10);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(5, 554700, "5인 이상", 10);
        insertPersonNum(personEntity);

        personEntity = new PersonEntity(1, 502100, "1인", 20);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(2, 537900, "2인", 20);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(3, 551200, "3인", 20);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(4, 677300, "4인", 20);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(5, 893400, "5인 이상", 20);
        insertPersonNum(personEntity);

        personEntity = new PersonEntity(1, 501200, "1인", 30);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(2, 522100, "2인", 30);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(3, 607700, "3인", 30);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(4, 793200, "4인", 30);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(5, 1000800, "5인 이상", 30);
        insertPersonNum(personEntity);

        personEntity = new PersonEntity(1, 498800, "1인", 40);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(2, 591400, "2인", 40);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(3, 681200, "3인", 40);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(4, 913500, "4인", 40);
        insertPersonNum(personEntity);
        personEntity = new PersonEntity(5, 1111400, "5인 이상", 40);
        insertPersonNum(personEntity);


        OptionEntity optionEntity = new OptionEntity();
        optionEntity.setName(OptionEntity.OFFICE);
        insertOption(optionEntity);
        optionEntity.setName(OptionEntity.RADDER);
        insertOption(optionEntity);
        optionEntity.setName(OptionEntity.AIR_CONDITIONAL);
        insertOption(optionEntity);
        optionEntity.setName(OptionEntity.SYSTEM_HANGER);
        insertOption(optionEntity);
        optionEntity.setName(OptionEntity.CLOSET);
        insertOption(optionEntity);
        optionEntity.setName(OptionEntity.BED);
        insertOption(optionEntity);
        optionEntity.setName(OptionEntity.BUSY);
        insertOption(optionEntity);
        optionEntity.setName(OptionEntity.SAVE_MOVE);
        insertOption(optionEntity);
        optionEntity.setName(OptionEntity.FAR);
        insertOption(optionEntity);
    }
}
