package dato;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import dato.Usuario;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USUARIO".
*/
public class UsuarioDao extends AbstractDao<Usuario, Long> {

    public static final String TABLENAME = "USUARIO";

    /**
     * Properties of entity Usuario.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Nombre = new Property(1, String.class, "nombre", false, "NOMBRE");
        public final static Property Foto = new Property(2, String.class, "foto", false, "FOTO");
        public final static Property RegId = new Property(3, String.class, "regId", false, "REG_ID");
        public final static Property IdMoodle = new Property(4, Long.class, "idMoodle", false, "ID_MOODLE");
    };


    public UsuarioDao(DaoConfig config) {
        super(config);
    }
    
    public UsuarioDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USUARIO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NOMBRE\" TEXT," + // 1: nombre
                "\"FOTO\" TEXT," + // 2: foto
                "\"REG_ID\" TEXT," + // 3: regId
                "\"ID_MOODLE\" LONG);"); // 4: idMoodle
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USUARIO\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Usuario entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String nombre = entity.getNombre();
        if (nombre != null) {
            stmt.bindString(2, nombre);
        }
 
        String foto = entity.getFoto();
        if (foto != null) {
            stmt.bindString(3, foto);
        }
 
        String regId = entity.getRegId();
        if (regId != null) {
            stmt.bindString(4, regId);
        }
 
        Long idMoodle = entity.getIdMoodle();
        if (idMoodle != null) {
            stmt.bindLong(5, idMoodle);
        }

    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Usuario readEntity(Cursor cursor, int offset) {
        Usuario entity = new Usuario( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // nombre
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // foto
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // regId
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // notaMax
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Usuario entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNombre(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFoto(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRegId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIdMoodle(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Usuario entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Usuario entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}