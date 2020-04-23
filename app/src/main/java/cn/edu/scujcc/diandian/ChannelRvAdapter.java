package cn.edu.scujcc.diandian;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelRvAdapter extends RecyclerView.Adapter<ChannelRvAdapter.ChannelRowHolder> {

    private ChannelLab lab = ChannelLab.getInstance();
    private ChannelClickListener listener;

    public ChannelRvAdapter(ChannelClickListener lis){
        this.listener = lis;
    }
    /**
     * 当需要新的一行时，此方法负责创建这一行对应的对象，即ChannelRowHolder对象
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ChannelRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_row,parent,false);
        ChannelRowHolder holder = new ChannelRowHolder(rowView);
        return holder;
    }

    /**
     * 用于确定列表总共有几行（即多少个ChannelRowHolder对象）
     * @return
     */
    @Override
    public int getItemCount() {
        return lab.getSize();
    }

    /**
     * 用于确定每一行内容是什么，即填充行中各个视图的内容
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ChannelRowHolder holder, int position) {
//        Log.d("DD","onBindViewHolder position="+position);
        Channel c = lab.getChannel(position);
//        Channel c = new Channel();
//        c.setTitle("中央"+(position+1)+"台");
//        c.setQuality("1080P");
//        c.setCover(R.drawable.cctv1);
        holder.bind(c);
    }

    /**
     * 单行布局对应的Java控制类
     */
    public class ChannelRowHolder extends RecyclerView.ViewHolder{
        private TextView title;  //频道标题
        private TextView quality; //频道清晰度
        private ImageView cover; //频道封面图片

        public ChannelRowHolder(@NonNull View row) {
            super(row);
            this.title = row.findViewById(R.id.channel_title);
            this.quality = row.findViewById(R.id.channel_quality);
            this.cover = row.findViewById(R.id.channel_cover);
            row.setOnClickListener((v) -> {
                int position = getLayoutPosition();
                Log.d("DianDian",position+"行被点击啦！");
                //TODO 调用实际的跳转代码
                listener.onChannelClick(position);
            });
        }

        /**
         * 自定义方法，用于向内部title提供数据
         * @param c
         */
        public void bind(Channel c){
            this.title.setText(c.getTitle());
            this.quality.setText(c.getQuality());
            this.cover.setImageResource(c.getCover());
        }
    }

    //自定义新接口
    public interface ChannelClickListener{
        public void onChannelClick(int position);
    }
}
