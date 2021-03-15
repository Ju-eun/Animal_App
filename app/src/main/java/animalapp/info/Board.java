package animalapp.info;

import android.provider.ContactsContract;

import com.google.firebase.firestore.ServerTimestamp;

public class Board {

    private String id;
    private String title;
    private String contents;
    private String Uid;
    private String key;
    @ServerTimestamp
    private ContactsContract.Data data;

    public Board(){
    }

    public Board(String id, String title, String contents,String Uid) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.Uid = Uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public ContactsContract.Data getData() {
        return data;
    }

    public void setData(ContactsContract.Data data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    @Override
    public String toString() {
        return "Board{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", Uid='" + Uid + '\'' +
                ", key='" + key + '\'' +
                ", data=" + data +
                '}';
    }
}
