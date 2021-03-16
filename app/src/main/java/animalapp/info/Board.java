package animalapp.info;

import android.provider.ContactsContract;

import com.google.firebase.firestore.ServerTimestamp;

public class Board {

    private String id;
    private String title;
    private String contents;
    private String Uid;
    private String key;
    private String email;
    private String view;
    private String board_fileName;


    @ServerTimestamp
    private ContactsContract.Data data;

    public Board(){
    }

    public Board(String id, String title, String contents,String view, String Uid, String board_fileName) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.view=view;
        this.Uid=Uid;
        this.board_fileName=board_fileName;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBoard_fileName() {
        return board_fileName;
    }

    public void setBoard_fileName(String board_fileName) {
        this.board_fileName = board_fileName;
    }


    @Override
    public String toString() {
        return "Board{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", Uid='" + Uid + '\'' +
                ", key='" + key + '\'' +
                ", email='" + email + '\'' +
                ", view='" + view + '\'' +
                ", board_fileName='" + board_fileName + '\'' +
                ", data=" + data +
                '}';
    }
}
