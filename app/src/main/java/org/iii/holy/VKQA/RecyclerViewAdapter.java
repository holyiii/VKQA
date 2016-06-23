package org.iii.holy.VKQA;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.iii.holy.model.Auth;
import org.iii.holy.model.VoteItem;
import org.iii.holy.model.detailKarmaRule;

import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends Adapter<ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static Auth mAuser = null;
    private static int Rtype = 0;
    private static int ProBoundary = 1000;
    private static String hostip = null;
    private static detailKarmaRule karmaRule;
    private Context context;
    private List data;
    private OnItemClickListener onItemClickListener;
    private View mfootview = null;

    public RecyclerViewAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
        hostip = ((MainActivity) context).getHostIp();
    }

    public void setKarmaRule(detailKarmaRule karmaRule) {
        this.karmaRule = karmaRule;

    }

    public void setAuth(Auth mAuser) {
        this.mAuser = mAuser;
    }

    public void clearAuth() {
        this.mAuser = null;
    }

    public int getQAListType() {
        return this.Rtype;
    }

    public void setQAListType(int Rtype) {
        this.Rtype = Rtype;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void hidefoot() {
        if (mfootview != null)
            mfootview.setVisibility(View.GONE);
    }

    public void showfoot() {
        if (mfootview != null)
            mfootview.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_base, parent,
                    false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_foot, parent,
                    false);
            mfootview = view;
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            //holder.tv.setText(data.get(position))
            //((ItemViewHolder) holder).tvContext.setText(data.get(position).get("context"));
            Map<String, String> item;
            item = (Map<String, String>) data.get(position);

            //((ItemViewHolder) holder).tvContext.setText(item.get("context"));
            ((ItemViewHolder) holder).tvContext.setText(Html.fromHtml(item.get("context")));
            ((ItemViewHolder) holder).tvUsrname.setText(item.get("user"));
            ((ItemViewHolder) holder).tvArticleID.setText(item.get("article_id"));
            ((ItemViewHolder) holder).tvDate.setText(item.get("postTime"));
            ((ItemViewHolder) holder).tvArticleInfo.setText(item.get("article_info"));
            ((ItemViewHolder) holder).tvKarma.setText(item.get("karma"));
            ((ItemViewHolder) holder).tvTag.setText(item.get("tag"));
            int karma = Integer.valueOf(((ItemViewHolder) holder).tvKarma.getText().toString());
            if (karma < ProBoundary)
                ((ItemViewHolder) holder).imgHead.setImageResource(R.mipmap.ic_userhead);
            else
                ((ItemViewHolder) holder).imgHead.setImageResource(R.mipmap.ic_prohead);
            //Hide Vote Start
            ((ItemViewHolder) holder).skVote.setVisibility(View.INVISIBLE);
            ((ItemViewHolder) holder).imgVoteBad.setVisibility(View.INVISIBLE);
            ((ItemViewHolder) holder).imgVoteGood.setVisibility(View.INVISIBLE);
            //Hide Vote End

            /*
            if (Rtype == 1) {
                ((ItemViewHolder) holder).skVote.setVisibility(View.INVISIBLE);
                ((ItemViewHolder) holder).imgVoteBad.setVisibility(View.INVISIBLE);
                ((ItemViewHolder) holder).imgVoteGood.setVisibility(View.INVISIBLE);
            } else {
                ((ItemViewHolder) holder).skVote.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).imgVoteBad.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).imgVoteGood.setVisibility(View.VISIBLE);

                if (mAuser != null && mAuser.isSuccess()) {
                    ((ItemViewHolder) holder).skVote.setEnabled(true);
                    //load progress status
                } else
                    ((ItemViewHolder) holder).skVote.setEnabled(false);

            }*/


            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }
        } else {
            Log.d("View Type", " " + holder.getItemViewType());
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    static class ItemViewHolder extends ViewHolder {

        TextView tvDate;
        TextView tvUsrname;
        TextView tvContext;
        TextView tvArticleID; //tv_articleID
        TextView tvArticleInfo; //tv_articleInfo
        TextView tvKarma; //tv_karma
        TextView tvTag;
        ImageView imgHead;
        ImageView imgVoteGood;
        ImageView imgVoteBad;
        SeekBar skVote;

        public ItemViewHolder(final View view) {
            super(view);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvUsrname = (TextView) view.findViewById(R.id.txtUsrName);
            tvContext = (TextView) view.findViewById(R.id.tv_context);
            tvArticleID = (TextView) view.findViewById(R.id.tv_articleID);
            imgHead = (ImageView) view.findViewById(R.id.imgUsr);
            tvTag=(TextView) view.findViewById(R.id.tv_tag);
            tvArticleInfo = (TextView) view.findViewById(R.id.tv_articleInfo);
            tvKarma = (TextView) view.findViewById(R.id.tv_karma);
            imgVoteGood = (ImageView) view.findViewById(R.id.img_VoteGood);
            imgVoteBad = (ImageView) view.findViewById(R.id.img_VoteBad);
            skVote = (SeekBar) view.findViewById(R.id.seekBar);
            skVote.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressChanged = 1;
                int preProgress = 1;

                public int vote(int type, boolean bQuestion, long Aid) {
                    int voteResult = 0;
                    Map<String, Object> sResponse = null;

                    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                    VoteItem votePara = new VoteItem(bQuestion, Aid);
                    String svotePara = gson.toJson(votePara);
                    svotePara = svotePara.replace("\n", "");
                    RestFulSrv voteRestobj = new RestFulSrv(view.getContext());
                    switch (type) {
                        case 2: //vote
                            sResponse = voteRestobj.post(hostip + "/api/voteup?", "vote", svotePara);
                            break;
                        case 0: //devote
                            sResponse = voteRestobj.post(hostip + "/api/votedown?", "vote", svotePara);
                            break;
                        default:
                    }

                    return voteResult;
                }

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    /*
                    Toast toast = Toast.makeText(view.getContext(),
                            "K1:", Toast.LENGTH_SHORT);
                    toast.show();
                    skVote.setProgress(1);
                    */
                    //load progress status here

                    if (progress == 2 && mAuser.getUser().getKarma() < karmaRule.getVote_up()) {
                        Log.d("warming", "Vote error");
                        Toast toast = Toast.makeText(view.getContext(),
                                "Vote error:", Toast.LENGTH_SHORT);
                        toast.show();
                        //recovery progress status
                    } else if (progress == 0 && mAuser.getUser().getKarma() < karmaRule.getVote_down()) {
                        Log.d("warming", "deVote error");
                        Toast toast = Toast.makeText(view.getContext(),
                                "Devote error:", Toast.LENGTH_SHORT);
                        toast.show();
                        //recovery progress status
                    } else {
                        progressChanged = progress;
                        boolean bQuestion = tvArticleInfo.getText().toString().contains("有深度");
                        long Aid = Long.parseLong(tvArticleID.getText().toString());
                        int voteNum=vote(progressChanged, bQuestion, Aid);
                    }

                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                    preProgress = seekBar.getProgress();
                    Log.d("warming", "seek Touch");

                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    Log.d("adapter", "seek bar progress:" + progressChanged);
                    switch (progressChanged) {
                        case 0: // deVote
                            imgVoteBad.setImageResource(R.mipmap.ic_bad30);
                            imgVoteGood.setImageResource(R.mipmap.ic_ggood30);
                            break;
                        case 2: //Vote
                            imgVoteGood.setImageResource(R.mipmap.ic_good30);
                            imgVoteBad.setImageResource(R.mipmap.ic_gbad30);
                            break;
                        default:
                            imgVoteGood.setImageResource(R.mipmap.ic_ggood30);
                            imgVoteBad.setImageResource(R.mipmap.ic_gbad30);

                    }
                }
            });
            imgVoteGood = (ImageView) view.findViewById(R.id.img_VoteGood);
            imgVoteBad = (ImageView) view.findViewById(R.id.img_VoteBad);
            //tvUsrname.setText("123");

        }
    }

    static class FootViewHolder extends ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }

    }
}