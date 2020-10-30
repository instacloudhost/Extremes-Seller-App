package com.instacloudhost.extremes.sections;

import android.app.DatePickerDialog;

import java.util.Calendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.instacloudhost.extremes.R;
import com.instacloudhost.extremes.model.MStatus;
import com.instacloudhost.extremes.remote.APIService;
import com.instacloudhost.extremes.remote.RetrofitClient;
import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

public class WindsForm extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private SharedPreferences token;
    private DatePickerDialog datepicker;
    private String iCategory;
    private String extremes = "extremeStorage";
    EditText dob;
    private EditText t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17;
    private Spinner s1, s2, s3, s4;
    private AwesomeValidation mAwesomeValidation;
    private ArrayAdapter<String> distAdapter;
    private String[] distItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        iCategory = intent.getStringExtra("category");
        setTitle(intent.getStringExtra("title"));
        token = getSharedPreferences(extremes,
                Context.MODE_PRIVATE);

        setContentView(R.layout.activity_winds_form);

        mAwesomeValidation = new AwesomeValidation(UNDERLABEL);
        mAwesomeValidation.setContext(this);
        mAwesomeValidation.addValidation(this, R.id.shopName, "[a-zA-Z\\s]+", R.string.err_name);
        mAwesomeValidation.addValidation(this, R.id.customerName, "[a-zA-Z\\s]+", R.string.err_customer_name);
        mAwesomeValidation.addValidation(this, R.id.pan, "^[a-zA-Z0-9]{10}$", R.string.err_pan);
        mAwesomeValidation.addValidation(this, R.id.mobile, "^[0-9]{10}$", R.string.err_mobile);
        mAwesomeValidation.addValidation(this, R.id.pin, "^[0-9]{6}$", R.string.err_pin);
        mAwesomeValidation.addValidation(this, R.id.email, "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", R.string.err_email);
        mAwesomeValidation.addValidation(this, R.id.vidano, "^[0-9a-zA-z]{10,15}$", R.string.err_van);
        mAwesomeValidation.addValidation(this, R.id.nominee, "[a-zA-Z\\s]+", R.string.err_name);
        mAwesomeValidation.addValidation(this, R.id.accountNo, "^[0-9]{9,25}$", R.string.err_account_no);
        mAwesomeValidation.addValidation(this, R.id.ifsc, "^[a-zA-z0-9]{9,20}$", R.string.err_name);

        state();
        relation();

        String[] ITEMS = {"No District"};
        distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, ITEMS);
        distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.district);
        spinner.setAdapter(distAdapter);

        String[] IT = {"Male", "Female"};
        ArrayAdapter<String> ad = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, IT);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner sp = (MaterialSpinner) findViewById(R.id.gender);
        sp.setAdapter(ad);

        t1 = (EditText) findViewById(R.id.shopName);
        t2 = (EditText) findViewById(R.id.aoro);
        t3 = (EditText) findViewById(R.id.pan);
        t4 = (EditText) findViewById(R.id.mobile);
        t5 = (EditText) findViewById(R.id.email);
        t6 = (EditText) findViewById(R.id.ahn);
        t7 = (EditText) findViewById(R.id.bankname);
        t8 = (EditText) findViewById(R.id.baddress);
        t9 = (EditText) findViewById(R.id.accountNo);
        t10 = (EditText) findViewById(R.id.ifsc);
        t11 = (EditText) findViewById(R.id.permanetAddress);
        t12 = (EditText) findViewById(R.id.dob);
        t13 = (EditText) findViewById(R.id.pin);
        t14 = (EditText) findViewById(R.id.fhname);
        t15 = (EditText) findViewById(R.id.vidano);
        t16 = (EditText) findViewById(R.id.nominee);
        t17 = (EditText) findViewById(R.id.customerName);
        s1 = (Spinner) findViewById(R.id.state);
        s2 = (Spinner) findViewById(R.id.district);
        s3 = (Spinner) findViewById(R.id.gender);
        s4 = (Spinner) findViewById(R.id.relation);


        dob=(EditText) findViewById(R.id.dob);
        dob.setInputType(InputType.TYPE_NULL);
        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    datepicker = new DatePickerDialog(WindsForm.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }
                            }, year, month, day);
                    datepicker.show();
                }
            }
        });
    }

    public void submitForm(View v) {
        mAwesomeValidation.clear();
        if(mAwesomeValidation.validate()) {
            progressBar();
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            APIService apiservice = retrofit.create(APIService.class);
            Call call = apiservice.addWinds(t1.getText().toString(), t2.getText().toString(), t3.getText().toString(),
                    t4.getText().toString(), t5.getText().toString(), t6.getText().toString(), t7.getText().toString(),
                    t8.getText().toString(), t9.getText().toString(), t10.getText().toString(), t11.getText().toString(),
                    t12.getText().toString(), t13.getText().toString(), t14.getText().toString(), t15.getText().toString(),
                    t16.getText().toString(), t17.getText().toString(), s1.getSelectedItem().toString(), s2.getSelectedItem().toString(),
                    s3.getSelectedItem().toString(), s4.getSelectedItem().toString(), token.getString("token", ""), token.getString("user_type", ""));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.body() != null) {
                        MStatus mwindform = (MStatus) response.body();
                        progressDialog.dismiss();
//                    System.out.println(mwindform);
                        if(mwindform.getStatus().equals("true")) {
                            Intent main = new Intent(getBaseContext(), WindsUploads.class);
                            main.putExtra("customer_id", mwindform.getMessage());
                            startActivity(main);
                            //setResult(Activity.RESULT_OK); //add this
                            //finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),mwindform.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void progressBar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Customer");
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(50);
        progressDialog.show();
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }

    private void state() {
        String[] ITEMS = {"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa",
                "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh",
                "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
                "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli",
                "Daman and Diu", "Delhi", "Lakshadweep", "Puducherry"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.state);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String state = parent.getItemAtPosition(position).toString();
                district(state);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void relation() {
        String[] IT = {"Father", "Mother", "Son", "Daughter", "Brother", "Sister", "Husband", "Wife"};
        ArrayAdapter<String> ad = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, IT);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner sp = (MaterialSpinner) findViewById(R.id.relation);
        sp.setAdapter(ad);
    }

    private void district(String str){
//        adapter.notifyDataSetChanged();
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.district);
        System.out.println(str);
        switch (str){
            case "Andhra Pradesh":
                distItem = new String[]{"Anantapur", "Chittoor", "East Godavari", "Guntur", "Krishna", "Kurnool", "Prakasam", "Srikakulam", "Sri Potti Sriramulu Nellore", "Visakhapatnam", "Vizianagaram", "West Godavari", "YSR District, Kadapa (Cuddapah)"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Arunachal Pradesh":
                distItem = new String[]{"Anjaw","Changlang","Dibang Valley","East Kameng","East Siang","Kra Daadi","Kurung Kumey","Lohit","Longding","Lower Dibang Valley","Lower Siang","Lower Subansiri","Namsai","Papum Pare","Siang","Tawang","Tirap","Upper Siang","Upper Subansiri","West Kameng","West Siang"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Assam":
                distItem = new String[]{"Baksa","Barpeta","Biswanath","Bongaigaon","Cachar","Charaideo","Chirang","Darrang","Dhemaji","Dhubri","Dibrugarh","Dima Hasao (North Cachar Hills)","Goalpara","Golaghat","Hailakandi","Hojai","Jorhat","Kamrup","Kamrup Metropolitan","Karbi Anglong","Karimganj","Kokrajhar","Lakhimpur","Majuli","Morigaon","Nagaon","Nalbari","Sivasagar","Sonitpur","South Salamara-Mankachar","Tinsukia","Udalguri","West Karbi Anglong"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Bihar":
                distItem = new String[]{"Araria","Arwal","Aurangabad","Banka","Begusarai","Bhagalpur","Bhojpur","Buxar","Darbhanga","East Champaran (Motihari)","Gaya","Gopalganj","Jamui","Jehanabad","Kaimur (Bhabua)","Katihar","Khagaria","Kishanganj","Lakhisarai","Madhepura","Madhubani","Munger (Monghyr)","Muzaffarpur","Nalanda","Nawada","Patna","Purnia (Purnea)","Rohtas","Saharsa","Samastipur","Saran","Sheikhpura","Sheohar","Sitamarhi","Siwan","Supaul","Vaishali","West Champaran"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Chhattisgarh":
                distItem = new String[]{"Balod","Baloda Bazar","Balrampur","Bastar","Bemetara","Bijapur","Bilaspur","Dantewada (South Bastar)","Dhamtari","Durg","Gariyaband","Janjgir-Champa","Jashpur","Kabirdham (Kawardha)","Kanker (North Bastar)","Kondagaon","Korba","Korea (Koriya)","Mahasamund","Mungeli","Narayanpur","Raigarh","Raipur","Rajnandgaon","Sukma","Surajpur  ","Surguja"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Goa":
                distItem = new String[]{"North Goa","South Goa"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Gujarat":
                distItem = new String[]{"Ahmedabad","Amreli","Anand","Aravalli","Banaskantha (Palanpur)","Bharuch","Bhavnagar","Botad","Chhota Udepur","Dahod","Dangs (Ahwa)","Devbhoomi Dwarka","Gandhinagar","Gir Somnath","Jamnagar","Junagadh","Kachchh","Kheda (Nadiad)","Mahisagar","Mehsana","Morbi","Narmada (Rajpipla)","Navsari","Panchmahal (Godhra)","Patan","Porbandar","Rajkot","Sabarkantha (Himmatnagar)","Surat","Surendranagar","Tapi (Vyara)","Vadodara","Valsad"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Haryana":
                distItem = new String[]{"Ambala","Bhiwani","Charkhi Dadri","Faridabad","Fatehabad","Gurgaon","Hisar","Jhajjar","Jind","Kaithal","Karnal","Kurukshetra","Mahendragarh","Mewat","Palwal","Panchkula","Panipat","Rewari","Rohtak","Sirsa","Sonipat","Yamunanagar"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Himachal Pradesh":
                distItem = new String[]{"Bilaspur","Chamba","Hamirpur","Kangra","Kinnaur","Kullu","Lahaul & Spiti","Mandi","Shimla","Sirmaur (Sirmour)","Solan","Una"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Jammu and Kashmir":
                distItem = new String[]{"Anantnag","Bandipore","Baramulla","Budgam","Doda","Ganderbal","Jammu","Kargil","Kathua","Kishtwar","Kulgam","Kupwara","Leh","Poonch","Pulwama","Rajouri","Ramban","Reasi","Samba","Shopian","Srinagar","Udhampur"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Jharkhand":
                distItem = new String[]{"Bokaro","Chatra","Deoghar","Dhanbad","Dumka","East Singhbhum","Garhwa","Giridih","Godda","Gumla","Hazaribag","Jamtara","Khunti","Koderma","Latehar","Lohardaga","Pakur","Palamu","Ramgarh","Ranchi","Sahibganj","Seraikela-Kharsawan","Simdega","West Singhbhum"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Karnataka":
                distItem = new String[]{"Bagalkot","Ballari (Bellary)","Belagavi (Belgaum)","Bengaluru (Bangalore) Rural","Bengaluru (Bangalore) Urban","Bidar","Chamarajanagar","Chikballapur","Chikkamagaluru (Chikmagalur)","Chitradurga","Dakshina Kannada","Davangere","Dharwad","Gadag","Hassan","Haveri","Kalaburagi (Gulbarga)","Kodagu","Kolar","Koppal","Mandya","Mysuru (Mysore)","Raichur","Ramanagara","Shivamogga (Shimoga)","Tumakuru (Tumkur)","Udupi","Uttara Kannada (Karwar)","Vijayapura (Bijapur)","Yadgir"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Kerala":
                distItem = new String[]{"Alappuzha","Ernakulam","Idukki","Kannur","Kasaragod","Kollam","Kottayam","Kozhikode","Malappuram","Palakkad","Pathanamthitta","Thiruvananthapuram","Thrissur","Wayanad"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Madhya Pradesh":
                distItem = new String[]{"Agar Malwa","Alirajpur","Anuppur","Ashoknagar","Balaghat","Barwani","Betul","Bhind","Bhopal","Burhanpur","Chhatarpur","Chhindwara","Damoh","Datia","Dewas","Dhar","Dindori","Guna","Gwalior","Harda","Hoshangabad","Indore","Jabalpur","Jhabua","Katni","Khandwa","Khargone","Mandla","Mandsaur","Morena","Narsinghpur","Neemuch","Panna","Raisen","Rajgarh","Ratlam","Rewa","Sagar","Satna","Sehore","Seoni","Shahdol","Shajapur","Sheopur","Shivpuri","Sidhi","Singrauli","Tikamgarh","Ujjain","Umaria","Vidisha"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Maharashtra":
                distItem = new String[]{"Ahmednagar","Akola","Amravati","Aurangabad","Beed","Bhandara","Buldhana","Chandrapur","Dhule","Gadchiroli","Gondia","Hingoli","Jalgaon","Jalna","Kolhapur","Latur","Mumbai City","Mumbai Suburban","Nagpur","Nanded","Nandurbar","Nashik","Osmanabad","Palghar","Parbhani","Pune","Raigad","Ratnagiri","Sangli","Satara","Sindhudurg","Solapur","Thane","Wardha","Washim","Yavatmal"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Manipur":
                distItem = new String[]{"Bishnupur","Chandel","Churachandpur","Imphal East","Imphal West","Jiribam","Kakching","Kamjong","Kangpokpi","Noney","Pherzawl","Senapati","Tamenglong","Tengnoupal","Thoubal","Ukhrul"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Meghalaya":
                distItem = new String[]{"East Garo Hills","East Jaintia Hills","East Khasi Hills","North Garo Hills","Ri Bhoi","South Garo Hills","South West Garo Hills ","South West Khasi Hills","West Garo Hills","West Jaintia Hills","West Khasi Hills"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Mizoram":
                distItem = new String[]{"Aizawl","Champhai","Kolasib","Lawngtlai","Lunglei","Mamit","Saiha","Serchhip"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Nagaland":
                distItem = new String[]{"Dimapur","Kiphire","Kohima","Longleng","Mokokchung","Mon","Peren","Phek","Tuensang","Wokha","Zunheboto"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Odisha":
                distItem = new String[]{"Angul","Balangir","Balasore","Bargarh", "Bhubaneswar", "Bhadrak","Boudh","Cuttack","Deogarh","Dhenkanal","Gajapati","Ganjam","Jagatsinghapur","Jajpur","Jharsuguda","Kalahandi","Kandhamal","Kendrapara","Kendujhar (Keonjhar)","Khordha","Koraput","Malkangiri","Mayurbhanj","Nabarangpur","Nayagarh","Nuapada","Puri","Rayagada","Sambalpur","Sonepur","Sundargarh"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Punjab":
                distItem = new String[]{"Amritsar","Barnala","Bathinda","Faridkot","Fatehgarh Sahib","Fazilka","Ferozepur","Gurdaspur","Hoshiarpur","Jalandhar","Kapurthala","Ludhiana","Mansa","Moga","Muktsar","Nawanshahr (Shahid Bhagat Singh Nagar)","Pathankot","Patiala","Rupnagar","Sahibzada Ajit Singh Nagar (Mohali)","Sangrur","Tarn Taran"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Rajasthan":
                distItem = new String[]{"Ajmer","Alwar","Banswara","Baran","Barmer","Bharatpur","Bhilwara","Bikaner","Bundi","Chittorgarh","Churu","Dausa","Dholpur","Dungarpur","Hanumangarh","Jaipur","Jaisalmer","Jalore","Jhalawar","Jhunjhunu","Jodhpur","Karauli","Kota","Nagaur","Pali","Pratapgarh","Rajsamand","Sawai Madhopur","Sikar","Sirohi","Sri Ganganagar","Tonk","Udaipur"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Sikkim":
                distItem = new String[]{"East Sikkim","North Sikkim","South Sikkim","West Sikkim"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Tamil Nadu":
                distItem = new String[]{"Ariyalur","Chennai","Coimbatore","Cuddalore","Dharmapuri","Dindigul","Erode","Kanchipuram","Kanyakumari","Karur","Krishnagiri","Madurai","Nagapattinam","Namakkal","Nilgiris","Perambalur","Pudukkottai","Ramanathapuram","Salem","Sivaganga","Thanjavur","Theni","Thoothukudi (Tuticorin)","Tiruchirappalli","Tirunelveli","Tiruppur","Tiruvallur","Tiruvannamalai","Tiruvarur","Vellore","Viluppuram","Virudhunagar"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Telangana":
                distItem = new String[]{"Adilabad","Bhadradri Kothagudem","Hyderabad","Jagtial","Jangaon","Jayashankar Bhoopalpally","Jogulamba Gadwal","Kamareddy","Karimnagar","Khammam","Komaram Bheem Asifabad","Mahabubabad","Mahabubnagar","Mancherial","Medak","Medchal","Nagarkurnool","Nalgonda","Nirmal","Nizamabad","Peddapalli","Rajanna Sircilla","Rangareddy","Sangareddy","Siddipet","Suryapet","Vikarabad","Wanaparthy","Warangal (Rural)","Warangal (Urban)","Yadadri Bhuvanagiri"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Tripura":
                distItem = new String[]{"Dhalai","Gomati","Khowai","North Tripura","Sepahijala","South Tripura","Unakoti","West Tripura"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Uttarakhand":
                distItem = new String[]{"Almora","Bageshwar","Chamoli","Champawat","Dehradun","Haridwar","Nainital","Pauri Garhwal","Pithoragarh","Rudraprayag","Tehri Garhwal","Udham Singh Nagar","Uttarkashi"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Uttar Pradesh":
                distItem = new String[]{"Agra","Aligarh","Allahabad","Ambedkar Nagar","Amethi (Chatrapati Sahuji Mahraj Nagar)","Amroha (J.P. Nagar)","Auraiya","Azamgarh","Baghpat","Bahraich","Ballia","Balrampur","Banda","Barabanki","Bareilly","Basti","Bhadohi","Bijnor","Budaun","Bulandshahr","Chandauli","Chitrakoot","Deoria","Etah","Etawah","Faizabad","Farrukhabad","Fatehpur","Firozabad","Gautam Buddha Nagar","Ghaziabad","Ghazipur","Gonda","Gorakhpur","Hamirpur","Hapur (Panchsheel Nagar)","Hardoi","Hathras","Jalaun","Jaunpur","Jhansi","Kannauj","Kanpur Dehat","Kanpur Nagar","Kanshiram Nagar (Kasganj)","Kaushambi","Kushinagar (Padrauna)","Lakhimpur - Kheri","Lalitpur","Lucknow","Maharajganj","Mahoba","Mainpuri","Mathura","Mau","Meerut","Mirzapur","Moradabad","Muzaffarnagar","Pilibhit","Pratapgarh","RaeBareli","Rampur","Saharanpur","Sambhal (Bhim Nagar)","Sant Kabir Nagar","Shahjahanpur","Shamali (Prabuddh Nagar)","Shravasti","Siddharth Nagar","Sitapur","Sonbhadra","Sultanpur","Unnao","Varanasi"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "West Bengal":
                distItem = new String[]{"Alipurduar","Bankura","Birbhum","Cooch Behar","Dakshin Dinajpur (South Dinajpur)","Darjeeling","Hooghly","Howrah","Jalpaiguri","Jhargram","Kalimpong","Kolkata","Malda","Murshidabad","Nadia","North 24 Parganas","Paschim Medinipur (West Medinipur)","Paschim (West) Burdwan (Bardhaman)","Purba Burdwan (Bardhaman)","Purba Medinipur (East Medinipur)","Purulia","South 24 Parganas","Uttar Dinajpur (North Dinajpur)"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Andaman and Nicobar Islands":
                distItem = new String[]{"Nicobar","North and Middle Andaman","South Andaman"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Chandigarh":
                distItem = new String[]{"Chandigarh"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Dadra and Nagar Haveli":
                distItem = new String[]{"Dadra & Nagar Haveli"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Daman and Diu":
                distItem = new String[]{"Daman","Diu"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Delhi":
                distItem = new String[]{"Central Delhi","East Delhi","New Delhi","North Delhi","North East  Delhi","North West  Delhi","Shahdara","South Delhi","South East Delhi","South West  Delhi","West Delhi"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Lakshadweep":
                distItem = new String[]{"Lakshadweep"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
            case "Puducherry":
                distItem = new String[]{"Karaikal","Mahe","Pondicherry","Yanam"};
                distAdapter = new ArrayAdapter<String>(WindsForm.this, android.R.layout.simple_spinner_item, distItem);
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(distAdapter);
                distAdapter.notifyDataSetChanged();
                break;
        }
    }

}