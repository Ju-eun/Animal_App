package animalapp.info;

import android.provider.ContactsContract;

import com.google.firebase.firestore.ServerTimestamp;

public class Board {

    private String id;
    private String title;
    private String contents;
    private String Uid;
    @ServerTimestamp
    private ContactsContract.Data data;

    public Board(){
    }

    public Board(String id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
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

    @Override
    public String toString() {
        return "Board{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", Uid='" + Uid + '\'' +
                ", data=" + data +
                '}';
    }
}
