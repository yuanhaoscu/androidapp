package com.yuanhao.helloworlddemo;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class DateTimePickerActivity extends Activity {
	
	private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btn_ok;
    private Button btn_cancel;
    int myear;
	int mmonth;
	int mday ;
	int mhour ;
	int mminute ;
	int onoff=1;
	RadioGroup radiogroup;  
    RadioButton radio_on,radio_off; 
    
 @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_layout);
 
       datePicker = (DatePicker) findViewById(R.id.dpPicker);
        timePicker = (TimePicker) findViewById(R.id.tpPicker);
        btn_ok=(Button)findViewById(R.id.btn_date_time_ok);
        btn_cancel=(Button)findViewById(R.id.btn_date_time_cancel);
        radiogroup=(RadioGroup)findViewById(R.id.group_onoff);
 		radio_on=(RadioButton)findViewById(R.id.radio_on); 
 		radio_off=(RadioButton)findViewById(R.id.radio_off); 
 		radio_on.setChecked(true);
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		 myear=t.year;
		 mmonth=t.month;
	     mday = t.monthDay;
		 mhour = t.hour;
		 mminute = t.minute;
		timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(mhour);
        timePicker.setCurrentMinute(mminute);
        
        datePicker.init(myear, mmonth, mday, new OnDateChangedListener() {

           @Override
            public void onDateChanged(DatePicker view, int year,
                   int monthOfYear, int dayOfMonth) {
               // 获取一个日历对象，并初始化为当前选中的时间
             
            }
       });

        
        
         timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                   public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    	
                       
	                     }
	                 });
         
        
 		
 		radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
             
             @Override  
             public void onCheckedChanged(RadioGroup group, int checkedId) {  

                 if(checkedId==radio_on.getId())  
                 {  
                     onoff=1;  
                 }else  
                 {  
                     onoff=0; 
                 }  
             }  
         }); 
 		
 		
         
         btn_ok.setOnClickListener(new OnClickListener() 
 		{
 			@Override
 			public void onClick(View v)
 			{
 				
 				Bundle bundle = new Bundle();
 				
                 bundle.putString("month",String.valueOf(datePicker.getMonth()+1));
                 bundle.putString("day", String.valueOf(datePicker.getDayOfMonth()));
                 bundle.putString("hour", String.valueOf(timePicker.getCurrentHour()));
                 bundle.putString("minute", String.valueOf(timePicker.getCurrentMinute()));
                 bundle.putString("onoff", String.valueOf(onoff));
                 
                 DateTimePickerActivity.this.setResult(RESULT_OK, DateTimePickerActivity.this.getIntent().putExtras(bundle));
                 DateTimePickerActivity.this.finish();
 			}
 		});
         
         btn_cancel.setOnClickListener(new OnClickListener() 
  		{
  			@Override
  			public void onClick(View v)
  			{
  				
  				Bundle bundle = new Bundle();
  				
                  bundle.putString("month",String.valueOf(datePicker.getMonth()+1));
                  bundle.putString("day", String.valueOf(datePicker.getDayOfMonth()));
                  bundle.putString("hour", String.valueOf(timePicker.getCurrentHour()));
                  bundle.putString("minute", String.valueOf(timePicker.getCurrentMinute()));
                  bundle.putString("onoff", String.valueOf(onoff));
                  
                  DateTimePickerActivity.this.setResult(RESULT_CANCELED, DateTimePickerActivity.this.getIntent().putExtras(bundle));
                  DateTimePickerActivity.this.finish();
  			}
  		});
	     }
}
