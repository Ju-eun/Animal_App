package animalapp.info;

public class MemInfo {

    private String id;
    private String pwd;
    private String name;
    private String phone;
    private String pet_name;
    private String pet_type;
    private String pet_gender;
    private String sign_profile;

    public MemInfo(String id, String pwd, String name, String phone, String pet_name, String pet_type, String pet_gender, String sign_profile) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.phone = phone;
        this.pet_name = pet_name;
        this.pet_type = pet_type;
        this.pet_gender = pet_gender;
        this.sign_profile = sign_profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getPet_type() {
        return pet_type;
    }

    public void setPet_type(String pet_type) {
        this.pet_type = pet_type;
    }

    public String getPet_gender() {
        return pet_gender;
    }

    public void setPet_gender(String pet_gender) {
        this.pet_gender = pet_gender;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSign_profile() {
        return sign_profile;
    }

    public void setSign_profile(String sign_profile) {
        this.sign_profile = sign_profile;
    }
}
