package com.yuanhao.helloworlddemo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;  
import android.database.sqlite.SQLiteOpenHelper;  
import android.graphics.Color;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences; 

public class MainActivity extends Activity 
{

	private TextView txtview1;
	private Button btn_onwork;
	private Button btn_offwork;
	private Button btn_statistic;
    private String tableName="tableZero";
    private String dataBaseName;
    private ListView m_listview;
    private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_main);
	//	txtview1=(TextView)findViewById(R.id.textView1);
		btn_onwork=(Button)findViewById(R.id.button_onwork);
		btn_offwork=(Button)findViewById(R.id.button_offwork);
		btn_statistic=(Button)findViewById(R.id.button_statistic);
		
		m_listview=(ListView)findViewById(R.id.listView1);
		txtview1=(TextView)findViewById(R.id.textView1);
		txtview1.setText("当前记录表名为："+tableName);
		
		context=this;
		RelativeLayout mLayout=(RelativeLayout)this.findViewById(R.id.main_layout);
	    mLayout.setBackgroundColor(0xFF6699FF);
        m_listview.setBackgroundColor(0XFF3366cc);
		ReadConfigFile();
		CreateOrOpenDB();
   
		
		btn_onwork.setOnClickListener(new OnClickListener() 
		{
			
			

			@Override
			public void onClick(View v)
			{
				
				Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
				t.setToNow(); // 取得系统时间。
				int month=t.month+1;
				int day = t.monthDay;
				int hour = t.hour;
				int minute = t.minute;
				int onoff=1;
				SQLiteDatabase db = openOrCreateDatabase(dataBaseName, Context.MODE_PRIVATE, null);  
		        String sqlexec="INSERT INTO '"+tableName.trim()+"' VALUES (NULL,?, ?, ?,?,?)";
		        db.execSQL(sqlexec,new Object[]{month,day,hour,minute,onoff} ); 
                db.close();
                RefreshListView();
			}
		});
		btn_offwork.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{

				Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
				t.setToNow(); // 取得系统时间。
				int month=t.month+1;
				int day = t.monthDay;
				int hour = t.hour;
				int minute = t.minute;
				int onoff=0;
				SQLiteDatabase db = openOrCreateDatabase(dataBaseName, Context.MODE_PRIVATE, null);  
		        String sqlexec="INSERT INTO '"+tableName.trim()+"' VALUES (NULL,?, ?, ?,?,?)";
		        db.execSQL(sqlexec,new Object[]{month,day,hour,minute,onoff} ); 
                db.close();
                RefreshListView();
			}
		});
		btn_statistic.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
                boolean flag1=false;
                int t=0;
                int prevd=0;
                int prevh=0;
                int prevm=0;
                int tmpd=0;
                int tmph=0;
                int tmpm=0;
				SQLiteDatabase db = openOrCreateDatabase(dataBaseName, Context.MODE_PRIVATE, null);
//				String sqlexec="INSERT INTO '"+tableName.trim()+"' VALUES (NULL,?, ?, ?,?,?)";
//		        db.execSQL(sqlexec,new Object[]{1,8,30,1} ); 
//		        db.execSQL(sqlexec,new Object[]{1,12,30,0} ); 
//		        db.execSQL(sqlexec,new Object[]{2,8,30,1} ); 
//		        db.execSQL(sqlexec,new Object[]{2,12,30,0} ); 
//		        db.execSQL(sqlexec,new Object[]{5,8,30,1} ); 
//		        db.execSQL(sqlexec,new Object[]{6,12,30,0} ); 
		        
		        Cursor c=db.rawQuery("SELECT * FROM '"+tableName.trim()+"' ORDER BY day", null);
		        if(c.moveToNext())
		        {
		             prevd = c.getInt(c.getColumnIndex("day"));  
		             prevh= c.getInt(c.getColumnIndex("hour"));
		             prevm=c.getInt(c.getColumnIndex("minute"));
		             int onoff=c.getInt(c.getColumnIndex("onoff"));
		             if(onoff==1)
		            	 flag1=true;
		             else
		            	 flag1=false;
		        };
		        
		        while(c.moveToNext())
		        {
		        	 tmpd = c.getInt(c.getColumnIndex("day"));  
		             tmph= c.getInt(c.getColumnIndex("hour"));
		             tmpm=c.getInt(c.getColumnIndex("minute"));
		             int onoff=c.getInt(c.getColumnIndex("onoff"));
		             if(flag1==true&&tmpd==prevd&&onoff==0)
		             {
		            	 int x=tmph*60+tmpm-(prevh*60+prevm);
		            	 if(x<0)
		            	 {
		            		 prevd=tmpd;
		            		 prevh=tmph;
		            		 prevm=tmpm;
		            		 flag1=false;
		            		 
		            	 }
		            	 else
		            	 {
		            		 
		            		 t=t+x;
		            		 flag1=false;
		            	 }
		             }
		             else
		             {
		            	 prevd=tmpd;
	            		 prevh=tmph;
	            		 prevm=tmpm;
	            		 if(onoff==1) flag1=true;
	            		 else flag1=false;
	            		 
		             }

		        }
		        AlertDialog.Builder builder = new AlertDialog.Builder(context);
		     //   DecimalFormat fmat=new DecimalFormat(".00");
		      //  builder.setTitle("本月你总共打卡："+fmat.format(t)+"小时");
		        builder.setTitle("本月你总共打卡："+t/60+"小时"+t%60+"分");
		        builder.setIcon(android.R.drawable.ic_dialog_info);
		        builder.setPositiveButton("Ok", null);
		        builder.show();
                db.close();
                
			}
		});
		
		
	}
	public void test()
	{
		SharedPreferences configinput = getSharedPreferences("config", 0); 
		    configinput.edit().putString("tableName", "tableZero").commit();
		    tableName="tableZero";
		    
		SQLiteDatabase db = openOrCreateDatabase(dataBaseName, Context.MODE_PRIVATE, null);
		
		String sqlexec="INSERT INTO '"+tableName.trim()+"' VALUES (NULL,?, ?, ?,?,?)";
		///////////////////////////////月     日     时   分    上下班
        db.execSQL(sqlexec,new Object[]{7,7,8,30,1} ); 
        db.execSQL(sqlexec,new Object[]{7,7,12,30,0} ); 
        db.execSQL(sqlexec,new Object[]{7,8,8,30,1} ); 
        db.execSQL(sqlexec,new Object[]{7,8,12,30,0} ); 
        db.execSQL(sqlexec,new Object[]{7,10,8,30,1} ); 
        db.execSQL(sqlexec,new Object[]{7,10,12,30,0} ); 
	}
	public void AddRecord(int month,int day,int hour,int minute,int onoff)
	{
		SQLiteDatabase db = openOrCreateDatabase(dataBaseName, Context.MODE_PRIVATE, null);  
        String sqlexec="INSERT INTO '"+tableName.trim()+"' VALUES (NULL,?, ?, ?,?,?)";
        db.execSQL(sqlexec,new Object[]{month,day,hour,minute,onoff} ); 
        db.close();
        RefreshListView();
	}
	public void ReadConfigFile()
	{
		SharedPreferences configinput = getSharedPreferences("config", 0); 
		if(configinput.getString("tableName",null)==null)
		    configinput.edit().putString("tableName", "tableZero").commit(); 
        if(configinput.getString("dataBaseName", null)==null)
        	configinput.edit().putString("dataBaseName", "newDatabase.db").commit();
        
		this.tableName=configinput.getString("tableName", "tableZero");
		this.dataBaseName=configinput.getString("dataBaseName", "newDatabase.db");
	}
	public void CreateOrOpenDB()
	{
		SQLiteDatabase db = openOrCreateDatabase(this.dataBaseName, Context.MODE_PRIVATE, null);  
      //  db.execSQL("DROP TABLE IF  EXISTS July");  
        //创建person表  
        db.execSQL("CREATE TABLE IF NOT EXISTS '"+tableName.trim()
        	+ "' (id INTEGER PRIMARY KEY AUTOINCREMENT,month SMALLINT, day SMALLINT, hour SMALLINT,minute SMALLINT,onoff SMALLINT)");  
        db.close();
        //////////////////////test
     //   test();
        ///////////////////////////
        this.RefreshListView();
        
	}
	
	public void RefreshListView()
	{
		SQLiteDatabase db = openOrCreateDatabase(this.dataBaseName, Context.MODE_PRIVATE, null);  

        String sqlexec="SELECT * FROM '"+this.tableName.trim()+"' ORDER BY day";
        Cursor c = db.rawQuery(sqlexec,null);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>(); 
        HashMap<String, Object> tmap = new HashMap<String, Object>(); 
        tmap.put("id", "ID"); 
        tmap.put("month_day", "日期");  
        tmap.put("hour_minute", "时间");
        tmap.put("onoff","上/下班"); 
        listItem.add(tmap);  
        while (c.moveToNext()) 
        {  
            int id = c.getInt(c.getColumnIndex("id")); 
            int month=c.getInt(c.getColumnIndex("month"));
            int day = c.getInt(c.getColumnIndex("day"));  
            int hour = c.getInt(c.getColumnIndex("hour"));
            int minute=c.getInt(c.getColumnIndex("minute"));
            int onoff=c.getInt(c.getColumnIndex("onoff"));
            String onoffs=new String();
            if(onoff==1) onoffs="上班";
            else onoffs="下班";
            		
            HashMap<String, Object> map = new HashMap<String, Object>(); 
            map.put("id", id);//图像资源的ID  
            map.put("month_day", month+"--"+day);  
            map.put("hour_minute", hour+":"+minute);
            map.put("onoff",  onoffs);
            listItem.add(map);  
          
          
        } 
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
                R.layout.item,//ListItem的XML实现  
                //动态数组与ImageItem对应的子项          
                new String[] {"id","month_day", "hour_minute","onoff"},   
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
                new int[] {R.id.item_id,R.id.item_month_day,R.id.item_hour_minute,R.id.item_onoff}  
            );  
             
            //添加并且显示  
            m_listview.setAdapter(listItemAdapter);  
            m_listview.setOnItemClickListener(new ItemClickListener()); 
            txtview1.setText("当前记录表名为："+tableName);
           c.close();  
        //关闭当前数据库  
           db.close(); 
	}
	
	public void DeleteItem(int id)
	{
		if(id==0) return;
		SQLiteDatabase db = openOrCreateDatabase(this.dataBaseName, Context.MODE_PRIVATE, null); 
		String sqlexec="DELETE FROM '"+tableName.trim()+"' WHERE id='"+id+"'";
		db.execSQL(sqlexec);
		db.close();
		RefreshListView();
	}
	
	private final class ItemClickListener implements OnItemClickListener
	{  
		public int pid=0;
		public int pos=0;
        public void onItemClick(AdapterView<?> parent, View view,  int position, long id) 
        {  
            ListView listView = (ListView) parent;  
            @SuppressWarnings("unchecked")
			HashMap<String, Object> data = (HashMap<String, Object>)listView.getItemAtPosition(position); 
            String personid = data.get("id").toString();
	        pos=position;
	     
	        if(pos==0)
                return;
	        else
	        	pid= Integer.parseInt(String.valueOf(personid));
	        
          //  Toast.makeText(getApplicationContext(), personid, 0).show(); 
	        AlertDialog.Builder builder = new AlertDialog.Builder(context);
	     //   builder.setTitle("是否要删除该项？");

	        builder.setTitle("你确定要删除该项记录吗？");
	        builder.setIcon(android.R.drawable.ic_dialog_info);
	        builder.setNegativeButton("Cancel", null);
	        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
	              if(pos!=0)
	              DeleteItem(pid);
	             }
	        });
	        builder.show();
        }  
    }  
	

	
	public void CreateNewTable(String tablename)
	{
		this.tableName=tablename;
		SharedPreferences configinput = getSharedPreferences("config", 0); 
		configinput.edit().putString("tableName", this.tableName).commit();
		
		SQLiteDatabase db = openOrCreateDatabase(this.dataBaseName, Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS '"+tableName.trim()
	        	+ "' (id INTEGER PRIMARY KEY AUTOINCREMENT,month SMALLINT, day SMALLINT, hour SMALLINT,minute SMALLINT,onoff SMALLINT)"); 

		db.close();
		this.RefreshListView();
	}
	public void DropTable(String tablename)
	{
		if(tablename.equals("tableZero")) return;
		SQLiteDatabase db = openOrCreateDatabase(this.dataBaseName, Context.MODE_PRIVATE, null);
		db.execSQL("DROP TABLE IF  EXISTS '"+tablename.trim() + "'");
		db.close();
		SharedPreferences configinput = getSharedPreferences("config", 0); 
		configinput.edit().putString("tableName", "tableZero").commit();
		this.tableName=configinput.getString("tableName", null);
		this.RefreshListView();
	}
    public void ClearTable()
    {
    	SQLiteDatabase db = openOrCreateDatabase(this.dataBaseName, Context.MODE_PRIVATE, null);
		db.execSQL("DROP TABLE IF  EXISTS '"+this.tableName.trim() + "'");
		db.execSQL("CREATE TABLE IF NOT EXISTS '"+tableName.trim()
	        	+ "' (id INTEGER PRIMARY KEY AUTOINCREMENT,month SMALLINT, day SMALLINT, hour SMALLINT,minute SMALLINT,onoff SMALLINT)");
		db.close();
		this.RefreshListView();
    }
    public void SwitchTable()
    {
    	 
    	 final EditText inputServer2 = new EditText(this);
	        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
	        builder2.setTitle("切换记录表：");
	        builder2.setIcon(android.R.drawable.ic_dialog_info);
	        builder2.setView(inputServer2);
	        builder2.setNegativeButton("Cancel", null);
	        builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
	               String name=inputServer2.getText().toString();
	               SQLiteDatabase db = openOrCreateDatabase(dataBaseName, Context.MODE_PRIVATE, null);
	   			   Cursor c =db.rawQuery("SELECT * FROM sqlite_master WHERE type='table' ORDER BY name",null);
	   			   boolean flag=false;
	   			    while(c.moveToNext())
			        {
			    	 String ts=c.getString(c.getColumnIndex("name"));
			    	 if(name.equals(ts))
			    		 flag=true;

			         }
	   			  if(flag)
	   			  {
	   				  tableName=name;
	   				  SharedPreferences configinput = getSharedPreferences("config", 0); 
	   				  configinput.edit().putString("tableName", tableName).commit();
	   				 
	   				  RefreshListView();
	   			  }
	   			 db.close();
	             }
	        });
	       
	        builder2.show();
    }
	@Override  
	
	  
	public boolean onCreateOptionsMenu(Menu menu)
   {  
	  
	   MenuInflater inflater = this.getMenuInflater();  
	  
	   inflater.inflate(R.menu.main, menu);  
	  
	   return true; 
	}
	@Override  
	  
	public boolean onOptionsItemSelected(MenuItem item)
	{  
	  
	   switch (item.getItemId())
	   {  
	  
	   case R.id.add_table:  
		   final EditText inputServer = new EditText(this);
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("添加记录表：");
	        builder.setIcon(android.R.drawable.ic_dialog_info);
	        builder.setView(inputServer);
	        builder.setNegativeButton("Cancel", null);
	        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
	               String tn=inputServer.getText().toString();
	               if(tn!=null&&!tn.equals("defaulttable"))
	               {
	            	   CreateNewTable(tn);
	               }
	             }
	        });
	        builder.show();
	  
	       break;  
	  
	   case R.id.drop_table:  
		   final EditText inputServer2 = new EditText(this);
	        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
	        builder2.setTitle("删除记录表：");
	        builder2.setIcon(android.R.drawable.ic_dialog_info);
	        builder2.setView(inputServer2);
	        builder2.setNegativeButton("Cancel", null);
	        builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
	               String tn=inputServer2.getText().toString();
	               if(tn!=null&&!tn.equals("defaulttable"))
	               {
	            	   DropTable(tn);
	               }
	             }
	        });
	        builder2.show();
	  
	       break; 
	   case R.id.clear_table:
		   
	        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
	        builder3.setTitle("你确定要清空记录表吗？不可恢复！建议单条删除不需要的记录！");
	        builder3.setIcon(android.R.drawable.ic_dialog_info);
	        builder3.setNegativeButton("Cancel", null);
	        builder3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
	 
	               ClearTable();
	             }
	        });
	        builder3.show();
	        break;
	   case R.id.show_all_table:
			SQLiteDatabase db = openOrCreateDatabase(this.dataBaseName, Context.MODE_PRIVATE, null);
			Cursor c =db.rawQuery("SELECT * FROM sqlite_master WHERE type='table' ORDER BY name",null);
			TextView tv=new TextView(this);
			String s=new String();
			String sys1=new String("android_metadata");
			String sys2=new String("sqlite_sequence");
		     while(c.moveToNext())
		     {
		    	 String ts=c.getString(c.getColumnIndex("name"));
		    	 if(!ts.equals(sys1))
		    	 {
		    	  if(!ts.equals(sys2))
		    	     s+= c.getString(c.getColumnIndex("name"))+"\r\n";
		    	 }
 
		     }
		     tv.setText(s);
		     tv.setTextSize(30);
		     tv.setTextColor(Color.rgb(255, 0, 0));
			 AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
			 builder4.setInverseBackgroundForced(true);
			 
		        builder4.setTitle("所有记录表：");
		        builder4.setIcon(android.R.drawable.ic_dialog_info);
		        builder4.setView(tv);
		        builder4.setNegativeButton("Cancel", null);
		        builder4.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

		            public void onClick(DialogInterface dialog, int which) {

		             }
		        });
		        db.close();
		        builder4.show();
		   break;
	   case R.id.switch_table:
		   SwitchTable();
		   break;
	   case R.id.add_record:
		//   Intent intent = new Intent(MainActivity.this,  
         //          AddRecordActivity.class);  
           // 启动指定Activity并等待返回的结果，其中0是请求码，用于标识该请求  
		   Intent intent = new Intent(MainActivity.this,  
			           DateTimePickerActivity.class);  
           startActivityForResult(intent,0);  
		   break;
		  
	   }  
	  
	   return true;
	}
	
	 @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        // TODO Auto-generated method stub
	        super.onActivityResult(requestCode, resultCode, data);
	         if (resultCode == RESULT_OK)
	         { 
	             Bundle bundle = data.getExtras(); 
	             int m=Integer.parseInt(bundle.getString("month"));
	             int d=Integer.parseInt(bundle.getString("day"));
	             int h=Integer.parseInt(bundle.getString("hour"));
	             int mint=Integer.parseInt(bundle.getString("minute"));
	             int onoff=Integer.parseInt(bundle.getString("onoff"));
	             this.AddRecord(m, d, h, mint, onoff);
	         }
	         else if (resultCode == RESULT_CANCELED)
	         { 
	          //  Toast.makeText(this, "未作任何修改", Toast.LENGTH_SHORT).show(); 
	         }
         }
}
