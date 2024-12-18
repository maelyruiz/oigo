package dato;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import dato.Mensaje;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MENSAJE".
*/
public class MensajeDao extends AbstractDao<Mensaje, Long> {

    public static final String TABLENAME = "MENSAJE";

    /**
     * Properties of entity Mensaje.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Texto = new Property(1, String.class, "texto", false, "TEXTO");
        public final static Property Fecha = new Property(2, String.class, "fecha", false, "FECHA");
        public final static Property Hora = new Property(3, String.class, "hora", false, "HORA");
        public final static Property Tipo = new Property(4, Boolean.class, "tipo", false, "TIPO");
        public final static Property IdContacto = new Property(5, Long.class, "idContacto", false, "ID_CONTACTO");
        public final static Property IdGrupo = new Property(6, Long.class, "idGrupo", false, "ID_GRUPO");
    };


    public MensajeDao(DaoConfig config) {
        super(config);
    }
    
    public MensajeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MENSAJE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TEXTO\" TEXT," + // 1: texto
                "\"FECHA\" TEXT," + // 2: fecha
                "\"HORA\" TEXT," + // 3: hora
                "\"TIPO\" INTEGER," + // 4: tipo
                "\"ID_CONTACTO\" INTEGER," + // 5: idContacto
                "\"ID_GRUPO\" INTEGER"); // 6: idGrupo
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MENSAJE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Mensaje entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String texto = entity.getTexto();
        if (texto != null) {
            stmt.bindString(2, texto);
        }
 
        String fecha = entity.getFecha();
        if (fecha != null) {
            stmt.bindString(3, fecha);
        }
 
        String hora = entity.getHora();
        if (hora != null) {
            stmt.bindString(4, hora);
        }
 
        Boolean tipo = entity.getTipo();
        if (tipo != null) {
            stmt.bindLong(5, tipo ? 1L: 0L);
        }
 
        Long idContacto = entity.getIdContacto();
        if (idContacto != null) {
            stmt.bindLong(6, idContacto);
        }

        Long idGrupo = entity.getIdGrupo();
        if (idContacto != null) {
            stmt.bindLong(6, idGrupo);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Mensaje readEntity(Cursor cursor, int offset) {
        Mensaje entity = new Mensaje( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // texto
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // fecha
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // hora
            cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0, // tipo
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // idContacto
                cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6)
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Mensaje entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTexto(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFecha(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setHora(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTipo(cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0);
        entity.setIdContacto(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setIdGrupo(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Mensaje entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Mensaje entity) {
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
