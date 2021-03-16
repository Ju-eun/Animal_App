package animalapp.info;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class MapActivity extends AppCompatActivity implements AutoPermissionsListener {

    private Button button1, button2;
    private TextView textView1;
    LocationManager manager;
    GPSListener gpsListener;

    SupportMapFragment mapFragment;
    GoogleMap map;

    Marker myMarker;
    MarkerOptions myLocationMarker;
    Circle circle;
    CircleOptions circle1KM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setTitle("GPS 현재위치 확인하기");

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        textView1 = findViewById(R.id.textView1);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsListener = new GPSListener();

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.i("MyLocTest", "지도 준비됨");
                map = googleMap;
                SeoulMarker();
                IncheonMarker();
                OsanMarker();
                SuwonMarker();
                CheonanMarker();
                DaejeonMarker();
                AsanMarker();
                SiheungMarker();
                AnyangMarker();
                GwangjuMarker();
                
                if ((ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                map.setMyLocationEnabled(true);
            }
        });

        // 최초 지도 숨김
        mapFragment.getView().setVisibility(View.GONE);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 지도 보임
                mapFragment.getView().setVisibility(View.VISIBLE);

                startLocationService();
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }
    public void SeoulMarker(){
        double[] a= {37.518766,37.52047976381448,37.5183072734096,37.52122427932994,37.518177831950005};
        double[] b= {126.933497,126.93333266712038,126.92617261328452,126.93433148605641,126.93289199793819};
        String animal[]= {"여의도 동물병원","우리 동물병원","I-PET동물병원","여의도 동물병원 대교점","여의도쿨펫동물병원"};
        String address[]={"서울특별시 영등포구 여의도동 국제금융로 108","서울특별시 영등포구 여의도동 54-6","서울특별시 영등포구 여의도동","서울특별시 영등포구 여의도동 여의대방로 417","서울특별시 영등포구 여의도동 42-1"};

        for(int i=0; i<a.length; i++)
        {
            LatLng seoul= new LatLng(a[i],b[i]);
            MarkerOptions markerOptions= new MarkerOptions();
            markerOptions
                    .position(seoul)
                    .title(animal[i])
                    .snippet(address[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .alpha(0.5f);
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul,10));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Toast.makeText(MapActivity.this, "눌렀습니다", Toast.LENGTH_SHORT).show();
                    return false;

                }
            });
        }


    }
    public void IncheonMarker()
    {
        double[] a= {37.39478219239067,37.396144116913284, 37.39433545578985,37.39132345587272,37.39456202177147};
        double[] b= {126.64111039793451,126.64580208258805,126.64077135375128,126.64807178444424,126.64734991328098};
        String animal[]= {"CENTRAL동물병원","베리떼동물병원","ACE동물병원","주주동물병원","미라클동물병원"};
        String address[]={"인천광역시 연수구 송도2동 23-4","인천광역시 연수구 해돋이로 168-7 레드원프라자","인천광역시 연수구 송도동 23-4 B동 101호","인천광역시 연수구 송도동 4-1","인천광역시 연수구 송도동 21-65"};

        for(int i=0; i<a.length; i++)
        {
            LatLng incheon= new LatLng(a[i],b[i]);
            MarkerOptions markerOptions= new MarkerOptions();
            markerOptions
                    .position(incheon)
                    .title(animal[i])
                    .snippet(address[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .alpha(0.5f);
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(incheon,10));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Toast.makeText(MapActivity.this, "눌렀습니다", Toast.LENGTH_SHORT).show();
                    return false;

                }
            });
        }
    }
    public void OsanMarker()
    {
        double[] a= {37.14940813413865, 37.17592014615883, 37.150098077635135,37.147506692398295, 37.17132262635789};
        double[] b= {127.07324723375935,127.04993929514195,127.07239731047233,127.07215604170304,127.06091711842718};
        String animal[]= {"오산동물병원","위더스 동물의료센터","25시종합동물병원 ","원동물병원 ","도래동물병원 "};
        String address[]={" 경기도 오산시 원동 754-3","경기도 오산시 금암동 50-2","경기도 오산시 오산동 869-36","경기도 오산시 원동 758-9","경기도 오산시 신장동 247-2"};

        for(int i=0; i<a.length; i++)
        {
            LatLng osan= new LatLng(a[i],b[i]);
            MarkerOptions markerOptions= new MarkerOptions();
            markerOptions
                    .position(osan)
                    .title(animal[i])
                    .snippet(address[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .alpha(0.5f);
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(osan,10));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Toast.makeText(MapActivity.this, "눌렀습니다", Toast.LENGTH_SHORT).show();
                    return false;

                }
            });
        }
    }
    public void SuwonMarker()
    {
        double[] a= {37.27487885238537, 37.27488512612975, 37.26732081191122, 37.26351796221968,  37.25522789050494, 37.241498354234196, 37.30045457251957, 37.298887527179325};
        double[] b= {127.04479446873722,127.04314138407726,127.03123391106686,127.02470881131467,127.0215904822317,127.02781263886449,127.01680798223288,126.97216686873789};
        String animal[]= {"수원종합동물병원","돌봄동물병원","전병준동물병원","24시꿈동물병원","수원펫동물병원","튼튼동물병원","조원동물병원","김성록동물병원"};
        String address[]={" 경기도 수원시 영통구 원천동 79-9","경기도 수원시 영통구 중부대로 250-1 KR","경기도 수원시 팔달구 인계동 1036","경기도 수원시 권선구 권선동 941-5","경기도 수원시 권선구 권선동 1058-4","경기도 수원시 권선구 곡반정동 565-10번지 KR","경기도 수원시 장안구 조원로 78 KR","경기도 수원시 장안구 율전동 288-6"};

        for(int i=0; i<a.length; i++)
        {
            LatLng suwon= new LatLng(a[i],b[i]);
            MarkerOptions markerOptions= new MarkerOptions();
            markerOptions
                    .position(suwon)
                    .title(animal[i])
                    .snippet(address[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .alpha(0.5f);
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(suwon,10));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Toast.makeText(MapActivity.this, "눌렀습니다", Toast.LENGTH_SHORT).show();
                    return false;

                }
            });
        }
    }
    public void CheonanMarker()
    {
        double[] a= {36.825369241515276,  36.82438832011575,  36.82792343822423,  36.83386680668568,   36.82362994541975,  36.81977094279913,  36.79908302739909,  36.80151863964647, 36.79523394092772,36.79404905303962,36.79851090179108 };
        double[] b= {127.13692989571398,127.12446833988905,127.13344935338407,127.13302095338432,127.15242938037387,127.15857101289885,127.15385011289823,127.13124596687838,127.12773543988804,127.12471779940319,127.11783203804316};
        String animal[]= {"늘푸른동물병원두정의료센터 ","나우동물의료센터 ","페트로동물병원 ","해맑은동물병원  ","고려동물병원 ","튼튼동물병원","애니펫동물병원 ","중앙동물병원 ","굿모닝24시동물병원 ","천안동물병원24 ","천안동물의료센터 ","김수영동물병원 "};
        String address[]={" 충청남도 천안시 서북구 성정2동 434-9","서북구 백석동 9-3번지 이현빌딩 1층 천안시 충청남도 KR","충청남도 천안시 서북구 성정2동 1230","충청남도 천안시 서북구 두정동 698","충청남도 천안시 동남구 신부동","충청남도 천안시 동남구 신부동 816","충청남도 천안시 동남구 원성2동","충청남도 천안시 서북구 쌍용1동","충청남도 천안시 서북구 쌍용1동","충청남도 천안시 서북구 쌍용1동","충청남도 천안시 서북구 쌍용3동"};

        for(int i=0; i<a.length; i++)
        {
            LatLng cheonan= new LatLng(a[i],b[i]);
            MarkerOptions markerOptions= new MarkerOptions();
            markerOptions
                    .position(cheonan)
                    .title(animal[i])
                    .snippet(address[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .alpha(0.5f);
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(cheonan,10));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Toast.makeText(MapActivity.this, "눌렀습니다", Toast.LENGTH_SHORT).show();
                    return false;

                }
            });
        }
    }
    public void DaejeonMarker()
    {
        double[] a= {36.34729980616137,   36.32919224893961,   36.3647764465077, 36.34729436731927, 36.350526130316176,  36.32011710246082};
        double[] b= {127.37709911104018,127.4051054993897,127.33892567055575,127.38624801288522,127.43663292638023,127.4152254552144};
        String animal[]= {"대전종합동물병원  ","대전24시센트럴동물병원  ","유성동물병원  ","쿨펫동물병원둔산점   ","웰니스 동물병원 대전복합터미널 ","대전우리동물의료센터 "};
        String address[]={" 대전광역시 서구 갈마2동 1438 ","대전광역시 중구 용두동 119-6","대전광역시 유성구 장대동 317-13"," 대전광역시 서구 탄방동 591","대전광역시 동구 용전동 63-3 대전복합터미널 3층","대전광역시 중구 대사동 248 365번지"};

        for(int i=0; i<a.length; i++)
        {
            LatLng daejeon= new LatLng(a[i],b[i]);
            MarkerOptions markerOptions= new MarkerOptions();
            markerOptions
                    .position(daejeon)
                    .title(animal[i])
                    .snippet(address[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .alpha(0.5f);
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(daejeon,10));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Toast.makeText(MapActivity.this, "눌렀습니다", Toast.LENGTH_SHORT).show();
                    return false;

                }
            });
        }
    }
    public void AsanMarker()
    {
        double[] a= {36.78704841228028, 36.77441210544032,  36.78647789859615,36.773392433809164,36.78883934727338};
        double[] b= {126.99997325522796,127.01149812823758,127.0055684668779,127.06978591105253,127.00870436835234};
        String animal[]= {"명동물병원  ","아산동물의료센터    ","온양동물병원   ","배방동물병원    ","가나동물병원  "};
        String address[]={" 충청남도 아산시 온양1동 1353 "," 충청남도 아산시 용화동 1581","충청남도 아산시 온양1동 1791","충청남도 아산시 배방읍 북수리 1213","충청남도 아산시 온양1동 80-5"};

        for(int i=0; i<a.length; i++)
        {
            LatLng asan= new LatLng(a[i],b[i]);
            MarkerOptions markerOptions= new MarkerOptions();
            markerOptions
                    .position(asan)
                    .title(animal[i])
                    .snippet(address[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .alpha(0.5f);
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(asan,10));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Toast.makeText(MapActivity.this, "눌렀습니다", Toast.LENGTH_SHORT).show();
                    return false;

                }
            });
        }
    }
    public void SiheungMarker()
    {
        double[] a= {37.45261727241489, 37.36420834259901, 37.4414401039385, 37.368161849233466, 37.45395571218937,37.35475615720481 };
        double[] b= {126.78391763928252,126.73007939757466,126.78388572641215,126.81208646504984,126.79009895682077,126.7230358264095};
        String animal[]= {"시흥동물병원   ","배곧 코코동물병원    ","펫츠나라동물병원   ","능곡동물병원    ","이지동물병원 ","시화종합동물병원 "};
        String address[]={"  경기도 시흥시 대야동 495 "," 경기도 시흥시 정왕동 함송로 15","경기도 시흥시 신천동 712-6","경기도 시흥시 능곡동 762","경기도 시흥시 대야동 562-5","경기도 시흥시 정왕동 1861-1"};

        for(int i=0; i<a.length; i++)
        {
            LatLng siheung= new LatLng(a[i],b[i]);
            MarkerOptions markerOptions= new MarkerOptions();
            markerOptions
                    .position(siheung)
                    .title(animal[i])
                    .snippet(address[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .alpha(0.5f);
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(siheung,10));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Toast.makeText(MapActivity.this, "눌렀습니다", Toast.LENGTH_SHORT).show();
                    return false;

                }
            });
        }
    }
    public void AnyangMarker()
    {
        double[] a= {37.402641388447606,37.396192222360156, 37.395695612937274, 37.39287298309062,37.38519115054256 };
        double[] b= {126.91780708223597,126.93116976874073,126.91824941476084,126.92672062456549,126.95985151107037};
        String animal[]= {"안양동물병원","안양비산동물병원","신동물병원","형제동물병원","평촌동물병원"};
        String address[]={" 경기도 안양시 만안구 안양3동 안양로 347"," 만안구 안양동 154-1번지 202호 안양시 경기도 KR","경기도 안양시 만안구 안양4동 711-129 KR","경기도 안양시 만안구 안양6동 414-5","경기도 안양시 동안구 귀인동 900-1"};

        for(int i=0; i<a.length; i++)
        {
            LatLng anyang= new LatLng(a[i],b[i]);
            MarkerOptions markerOptions= new MarkerOptions();
            markerOptions
                    .position(anyang)
                    .title(animal[i])
                    .snippet(address[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .alpha(0.5f);
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(anyang,10));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Toast.makeText(MapActivity.this, "눌렀습니다", Toast.LENGTH_SHORT).show();
                    return false;

                }
            });
        }
    }
    public void GwangjuMarker()
    {
        double[] a= {35.19100581220474,35.16183385889301,35.1905378453599,35.14145138718644,35.18628226703385,35.17505712323511};
        double[] b= {126.85778708217234,126.8840905128515,126.82813559751237,126.88703369935598,126.89887471285229,126.87605588217195};
        String animal[]= {"광주동물메디컬센터","본펫동물병원","수완종합동물병원","바로동물병원","동행동물병원","24시블루밍동물병원"};
        String address[]={"광주광역시 광산구 신창동 77-271"," 광주광역시 서구 광천동 무진대로 945-1 1층"," 광주광역시 광산구 수완동 1452","광주광역시 남구 주월2동 621-1","광주광역시 북구 오치동 785-6","광주광역시 북구 운암동 68 벽산블루밍 2단지상가 214호"};

        for(int i=0; i<a.length; i++)
        {
            LatLng gwangju= new LatLng(a[i],b[i]);
            MarkerOptions markerOptions= new MarkerOptions();
            markerOptions
                    .position(gwangju)
                    .title(animal[i])
                    .snippet(address[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .alpha(0.5f);
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(gwangju,10));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Toast.makeText(MapActivity.this, "눌렀습니다", Toast.LENGTH_SHORT).show();
                    return false;

                }
            });
        }
    }



    public void startLocationService() {
        try {
            Location location = null;

            long minTime = 0;        // 0초마다 갱신 - 바로바로갱신
            float minDistance = 0;

            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String message = "최근 위치1 -> Latitude : " + latitude + "\n Longitude : " + longitude;

                    textView1.setText(message);
                    showCurrentLocation(latitude, longitude);
                    Log.i("MyLocTest", "최근 위치1 호출");
                }

                //위치 요청하기
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
                //manager.removeUpdates(gpsListener);
                Toast.makeText(getApplicationContext(), "내 위치1확인 요청함", Toast.LENGTH_SHORT).show();
                Log.i("MyLocTest", "requestLocationUpdates() 내 위치1에서 호출시작 ~~ ");

            } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String message = "최근 위치2 -> Latitude : " + latitude + "\n Longitude : " + longitude;

                    textView1.setText(message);
                    showCurrentLocation(latitude, longitude);

                    Log.i("MyLocTest", "최근 위치2 호출");
                }


                //위치 요청하기
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, gpsListener);
                //manager.removeUpdates(gpsListener);
                Toast.makeText(getApplicationContext(), "내 위치2확인 요청함", Toast.LENGTH_SHORT).show();
                Log.i("MyLocTest", "requestLocationUpdates() 내 위치2에서 호출시작 ~~ ");
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    class GPSListener implements LocationListener {

        // 위치 확인되었을때 자동으로 호출됨 (일정시간 and 일정거리)
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String message = "내 위치는 Latitude : " + latitude + "\nLongtitude : " + longitude;
            textView1.setText(message);
            Log.i("MyLocTest", "onLocationChanged() 호출되었습니다.");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        // GPS provider를 이용전에 퍼미션 체크
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), "접근 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {

            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
                //manager.removeUpdates(gpsListener);
            } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsListener);
                //manager.removeUpdates(gpsListener);
            }

            if (map != null) {
                map.setMyLocationEnabled(true);
            }
            Log.i("MyLocTest", "onResume에서 requestLocationUpdates() 되었습니다.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.removeUpdates(gpsListener);

        if (map != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(false);
        }
        Log.i("MyLocTest","onPause에서 removeUpdates() 되었습니다.");
    }

    private void showCurrentLocation(double latitude, double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
    }

    private void showMyLocationMarker(LatLng curPoint) {
        if (myLocationMarker == null) {
            myLocationMarker = new MarkerOptions(); // 마커 객체 생성
            myLocationMarker.position(curPoint);
            myLocationMarker.title("최근위치 \n");
            myLocationMarker.snippet("*GPS로 확인한 최근위치");
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource((R.drawable.ic_map)));
          //  myMarker = map.addMarker(myLocationMarker);
        } else {
            myMarker.remove(); // 마커삭제
            myLocationMarker.position(curPoint);
            myMarker = map.addMarker(myLocationMarker);
        }

        // 반경추가
        if (circle1KM == null) {
            circle1KM = new CircleOptions().center(curPoint) // 원점
                    .radius(1000)       // 반지름 단위 : m
                    .strokeWidth(1.0f);    // 선너비 0f : 선없음
            //.fillColor(Color.parseColor("#1AFFFFFF")); // 배경색
            circle = map.addCircle(circle1KM);

        } else {
            circle.remove(); // 반경삭제
            circle1KM.center(curPoint);
            circle = map.addCircle(circle1KM);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
        Toast.makeText(this, "requestCode : "+requestCode+"  permissions : "+permissions+"  grantResults :"+grantResults, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
        Toast.makeText(getApplicationContext(),"permissions denied : " + permissions.length, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
        Toast.makeText(getApplicationContext(),"permissions granted : " + permissions.length, Toast.LENGTH_SHORT).show();
    }
}