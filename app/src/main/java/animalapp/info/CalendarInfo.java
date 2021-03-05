package animalapp.info;

public class CalendarInfo {

    private String state;
    private String uid;
    private String date;
    private String memo;

    public CalendarInfo(){

    }
    public CalendarInfo(String date, String memo, String uid, String state)
    {
        this.state=state;
        this.uid=uid;
        this.date=date;
        this.memo=memo;
    }
}
