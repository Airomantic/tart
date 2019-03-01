package com.example.list_dome;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity {

    public ListView listView;
    //创建ImageLoader对象
    private ImageLoader imageLoader = ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Bmob.initialize(this, "4943e6a9dd93e0df1aee0fc6d54239d9");
        queryPage();
        // 缓存图片的配置，一般通用的配置
        imageLoader.init(ImageLoaderConfiguration.createDefault(MainActivity.this));
    }


    public void init(){
        listView = (ListView) findViewById(R.id.main_list_view);
    }

    //查询所有数据
    public void queryPage(){
        BmobQuery<list> query = new BmobQuery<>();
        query.addWhereExists("objectId");
        query.findObjects(new FindListener<list>() {
            @Override
            public void done(List<list> list, BmobException e) {
                if (e == null) {
                    try {
                        listView.setAdapter(new NewListAdapter(list));
                        show("更新新闻" + list.size()+"条");
                    }catch (Exception es){
                        es.printStackTrace();
                    }
                }else {
                    show(e.toString());
                }
            }
        });
    }
    private void show(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    public class NewListAdapter extends BaseAdapter {
        private List<list> lists= new ArrayList<>();

        public NewListAdapter(List<list> list) {
            this.lists=list;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.item_news_list, null);
                viewHolder.titleContent = (TextView) convertView.findViewById(R.id.title_content);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.title_pic);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // 创建DisplayImageOptions对象并进行相关选项配置
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_launcher_background)// 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.drawable.ic_launcher_background)// 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.drawable.ic_launcher_background)// 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                    .displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
                    .build();// 创建DisplayImageOptions对象
            // 使用ImageLoader加载图片
            imageLoader.displayImage(lists.get(position).getIcon().getFileUrl(),viewHolder.icon);
            viewHolder.titleContent.setText(lists.get(position).getName());
            return  convertView;
        }

        public class  ViewHolder{
            TextView titleContent;
            ImageView icon;
        }
    }
    @Override
    protected void onDestroy() {
        // 回收该页面缓存在内存中的图片
        imageLoader.clearMemoryCache();
        super.onDestroy();
    }
}
