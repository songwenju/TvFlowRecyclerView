package com.lenovo.tvflowrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_EIGHT;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_EIGHTEEN;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_ELEVEN;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_FIFTEEN;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_FIVE;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_FOUR;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_FOURTEEN;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_NINE;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_NINETEEN;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_ONE;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_SEVEN;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_SEVENTEEN;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_SIX;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_SIXTEEN;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_TEN;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_THIRTEEN;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_THREE;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_TWELVE;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_TWENTY;
import static com.lenovo.tvflowrecyclerview.Constants.ITEM_TYPE_TWO;


/**
 * @author songwenju
 */
public class VodHomeAdapter extends RecyclerView.Adapter {
    private static final String TAG = "VodHomeAdapter";
    public static final int ITEM_LAYOUT_ONE = R.layout.item_type_1;
    public static final int ITEM_LAYOUT_TWO = R.layout.item_type_2;
    public static final int ITEM_LAYOUT_THREE = R.layout.item_type_3;
    public static final int ITEM_LAYOUT_FOUR = R.layout.item_type_4;
    public static final int ITEM_LAYOUT_FIVE = R.layout.item_type_5;
    public static final int ITEM_LAYOUT_SIX = R.layout.item_type_6;
    public static final int ITEM_LAYOUT_SEVEN = R.layout.item_type_7;
    public static final int ITEM_LAYOUT_EIGHT = R.layout.item_type_8;
    public static final int ITEM_LAYOUT_NINE = R.layout.item_type_9;
    public static final int ITEM_LAYOUT_TEN = R.layout.item_type_10;
    public static final int ITEM_LAYOUT_ELEVEN = R.layout.item_type_11;
    public static final int ITEM_LAYOUT_TWELVE = R.layout.item_type_12;
    public static final int ITEM_LAYOUT_THIRTEEN = R.layout.item_type_13;
    public static final int ITEM_LAYOUT_FOURTEEN = R.layout.item_type_14;
    public static final int ITEM_LAYOUT_FIFTEEN = R.layout.item_type_15;
    public static final int ITEM_LAYOUT_SIXTEEN = R.layout.item_type_16;
    public static final int ITEM_LAYOUT_SEVENTEEN = R.layout.item_type_17;
    public static final int ITEM_LAYOUT_EIGHTEEN = R.layout.item_type_18;
    public static final int ITEM_LAYOUT_NINETEEN = R.layout.item_type_19;
    public static final int ITEM_LAYOUT_TWENTY = R.layout.item_type_20;

    private Context mContext;
    private DisplayMetrics displayMetrics;
    private int mScreenWidth;
    private List<Module> mChannelModules;
    List<Module> elements = new ArrayList<>();

    public VodHomeAdapter(Context context, List<Module> moduleList) {
        this.mContext = context;
        this.mChannelModules = moduleList;
        LogUtil.i(this, "VodHomeAdapter.VodHomeAdapter.mChannelModules:" + mChannelModules);
        displayMetrics = context.getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;


        for (int i = 0; i < 20; i++) {
            Module module = new Module();
            module.setPoster("http://smtv-cms.oss-cn-beijing.aliyuncs.com/cms/2017-12-12/201712121055205457580.jpg");
            elements.add(module);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //to resolve the error:parameter must be a descendant of this view
        View currentFocus = ((Activity) mContext).getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
        return CommonRecyclerViewHolder.get(mContext, parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommonRecyclerViewHolder comHolder = (CommonRecyclerViewHolder) holder;

        LogUtil.i(this, "VodHomeAdapter.VodHomeAdapter.mChannelModules:" + mChannelModules);
        setTitle(comHolder);

        LinearLayout ll;

        switch (getItemViewType(position)) {
            case ITEM_LAYOUT_ONE: {
                ll = comHolder.getHolder().getView(R.id.layout_one);
                int[] ids = {R.id.sub_type1_image_one};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_TWO: {
                ll = comHolder.getHolder().getView(R.id.layout_two);
                int[] ids = {R.id.sub_type2_image_one, R.id.sub_type2_image_two};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_THREE: {
                ll = comHolder.getHolder().getView(R.id.layout_three);
                int[] ids = {R.id.sub_type3_image_one, R.id.sub_type3_image_two, R.id.sub_type3_image_three};
                setImgResources(ids, ll, elements);
                break;
            }
            case ITEM_LAYOUT_FOUR: {
                ll = comHolder.getHolder().getView(R.id.layout_four);
                int[] ids = {R.id.sub_type4_image_one, R.id.sub_type4_image_two};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_FIVE: {
                ll = comHolder.getHolder().getView(R.id.layout_five);
                int[] ids = {R.id.sub_type5_image_one, R.id.sub_type5_image_two, R.id.sub_type5_image_three, R.id.sub_type5_image_four
                        , R.id.sub_type5_image_five, R.id.sub_type5_image_six};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_SIX: {
                ll = comHolder.getHolder().getView(R.id.layout_six);
                int[] ids = {R.id.sub_type6_image_one, R.id.sub_type6_image_two, R.id.sub_type6_image_three, R.id.sub_type6_image_four
                        , R.id.sub_type6_image_five};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_SEVEN: {
                ll = comHolder.getHolder().getView(R.id.layout_seven);
                int[] ids = {R.id.sub_type7_layout_one, R.id.sub_type7_layout_two, R.id.sub_type7_layout_three, R.id.sub_type7_layout_four
                        , R.id.sub_type7_layout_five, R.id.sub_type7_layout_six, R.id.sub_type7_layout_seven};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_EIGHT: {
                ll = comHolder.getHolder().getView(R.id.layout_eight);
                int[] ids = {R.id.sub_type8_image_one, R.id.sub_type8_image_two, R.id.sub_type8_image_three, R.id.sub_type8_image_four
                        , R.id.sub_type8_image_five, R.id.sub_type8_image_six};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_NINE: {
                ll = comHolder.getHolder().getView(R.id.layout_nine);
                int[] ids = {R.id.sub_type1_image_one, R.id.sub_type2_image_one, R.id.sub_type2_image_one};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_TEN: {
                ll = comHolder.getHolder().getView(R.id.layout_ten);
                int[] ids = {R.id.sub_type1_image_one, R.id.sub_type3_image_one, R.id.sub_type3_image_two, R.id.sub_type3_image_three};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_ELEVEN: {
                ll = comHolder.getHolder().getView(R.id.layout_eleven);
                int[] ids = {R.id.sub_type2_image_one, R.id.sub_type2_image_two, R.id.sub_type3_image_one, R.id.sub_type3_image_two, R.id.sub_type3_image_three};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_TWELVE: {
                ll = comHolder.getHolder().getView(R.id.layout_twelve);
                int[] ids = {R.id.sub_type2_image_one, R.id.sub_type2_image_two, R.id.sub_type5_image_one, R.id.sub_type5_image_two
                        , R.id.sub_type5_image_three, R.id.sub_type5_image_four, R.id.sub_type5_image_five, R.id.sub_type5_image_six};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_THIRTEEN: {
                ll = comHolder.getHolder().getView(R.id.layout_thirteen);
                int[] ids = {R.id.sub_type3_image_one, R.id.sub_type3_image_two, R.id.sub_type3_image_three, R.id.sub_type5_image_one
                        , R.id.sub_type5_image_two, R.id.sub_type5_image_three, R.id.sub_type5_image_four, R.id.sub_type5_image_five
                        , R.id.sub_type5_image_six};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_FOURTEEN: {
                ll = comHolder.getHolder().getView(R.id.layout_fourteen);
                int[] ids = {R.id.sub_type14_image_one, R.id.sub_type14_image_two, R.id.sub_type14_image_three, R.id.sub_type14_image_four
                        , R.id.sub_type5_image_one, R.id.sub_type5_image_two, R.id.sub_type5_image_three, R.id.sub_type5_image_four
                        , R.id.sub_type5_image_five, R.id.sub_type5_image_six};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_FIFTEEN: {
                ll = comHolder.getHolder().getView(R.id.layout_fifteen);
                int[] ids = {R.id.sub_type4_image_one, R.id.sub_type4_image_two, R.id.sub_type5_image_one
                        , R.id.sub_type5_image_two, R.id.sub_type5_image_three, R.id.sub_type5_image_four, R.id.sub_type5_image_five
                        , R.id.sub_type5_image_six};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_SIXTEEN: {
                ll = comHolder.getHolder().getView(R.id.layout_sixteenn);
                LinearLayout viewInclude1 = ll.findViewById(R.id.sub_item_type_5_include1);
                LinearLayout viewInclude2 = ll.findViewById(R.id.sub_item_type_5_include2);
                int[] ids = {R.id.sub_type5_image_one, R.id.sub_type5_image_two, R.id.sub_type5_image_three, R.id.sub_type5_image_four
                        , R.id.sub_type5_image_five, R.id.sub_type5_image_six};

                setImgResources(ids, viewInclude1, elements.subList(0, elements.size() / 2));
                setImgResources(ids, viewInclude2, elements.subList(elements.size() / 2, elements.size()));
                break;
            }

            case ITEM_LAYOUT_SEVENTEEN: {
                ll = comHolder.getHolder().getView(R.id.layout_seventeen);
                int[] ids = {R.id.sub_type12_image_one, R.id.sub_type12_image_two, R.id.sub_type13_image_one, R.id.sub_type13_image_two
                        , R.id.sub_type13_image_three, R.id.sub_type13_image_four, R.id.sub_type13_image_five, R.id.sub_type13_image_six};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_EIGHTEEN: {
                ll = comHolder.getHolder().getView(R.id.layout_eighteen);
                int[] ids = {R.id.sub_type9_image_one, R.id.sub_type9_image_two, R.id.sub_type9_image_three, R.id.sub_type13_image_one
                        , R.id.sub_type13_image_two, R.id.sub_type13_image_three, R.id.sub_type13_image_four, R.id.sub_type13_image_five
                        , R.id.sub_type13_image_six};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_NINETEEN: {
                ll = comHolder.getHolder().getView(R.id.layout_nineteen);
                int[] ids = {R.id.sub_type9_image_one, R.id.sub_type9_image_two, R.id.sub_type9_image_three, R.id.sub_type3_image_one
                        , R.id.sub_type3_image_two, R.id.sub_type3_image_three};
                setImgResources(ids, ll, elements);
                break;
            }

            case ITEM_LAYOUT_TWENTY: {
                ll = comHolder.getHolder().getView(R.id.layout_twenty);
                int[] ids = {R.id.sub_type20_image_one, R.id.sub_type20_image_two};
                setImgResources(ids, ll, elements);
                break;
            }

            default:
                break;
        }
    }


    /**
     * 设置FocusRoundImageView资源
     *
     * @param ids view id 数组
     * @param ll  数据data
     */
    private void setImgResources(int[] ids, LinearLayout ll, List<Module> elements) {
        for (int i = 0; i < elements.size(); i++) {
            if (i >= ids.length) {
                return;
            }
            if (ll != null) {
                if (ll.findViewById(ids[i]) instanceof FocusRoundImageView) {

                    FocusRoundImageView focusRoundImageView = ll.findViewById(ids[i]);
                    final Module element = elements.get(i);

                    focusRoundImageView.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .load(element.getPoster())
                            .into(focusRoundImageView);
                }
            }

        }
    }


    /**
     * 设置标题，通用接口
     *
     * @param comHolder
     */

    private void setTitle(CommonRecyclerViewHolder comHolder) {
        TextView titleLayout = (TextView) comHolder.getHolder().getView(R.id.item_title_layout);
        titleLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemViewType(int position) {
        LogUtil.i(this, "VodHomeAdapter.getItemViewType,position:" + position + ",type:" + mChannelModules.get(position).getStyleType());

        if (mChannelModules.size() > position && position >= 0) {
            Module module = mChannelModules.get(position);

            switch (module.getStyleType()) {
                case ITEM_TYPE_ONE:
                    return ITEM_LAYOUT_ONE;
                case ITEM_TYPE_TWO:
                    return ITEM_LAYOUT_TWO;
                case ITEM_TYPE_THREE:
                    return ITEM_LAYOUT_THREE;
                case ITEM_TYPE_FOUR:
                    return ITEM_LAYOUT_FOUR;
                case ITEM_TYPE_FIVE:
                    return ITEM_LAYOUT_FIVE;
                case ITEM_TYPE_SIX:
                    return ITEM_LAYOUT_SIX;
                case ITEM_TYPE_SEVEN:
                    return ITEM_LAYOUT_SEVEN;
                case ITEM_TYPE_EIGHT:
                    return ITEM_LAYOUT_EIGHT;
                case ITEM_TYPE_NINE:
                    return ITEM_LAYOUT_NINE;
                case ITEM_TYPE_TEN:
                    return ITEM_LAYOUT_TEN;
                case ITEM_TYPE_ELEVEN:
                    return ITEM_LAYOUT_ELEVEN;
                case ITEM_TYPE_TWELVE:
                    return ITEM_LAYOUT_TWELVE;
                case ITEM_TYPE_THIRTEEN:
                    return ITEM_LAYOUT_THIRTEEN;
                case ITEM_TYPE_FOURTEEN:
                    return ITEM_LAYOUT_FOURTEEN;
                case ITEM_TYPE_FIFTEEN:
                    return ITEM_LAYOUT_FIFTEEN;
                case ITEM_TYPE_SIXTEEN:
                    return ITEM_LAYOUT_SIXTEEN;
                case ITEM_TYPE_SEVENTEEN:
                    return ITEM_LAYOUT_SEVENTEEN;
                case ITEM_TYPE_EIGHTEEN:
                    return ITEM_LAYOUT_EIGHTEEN;
                case ITEM_TYPE_NINETEEN:
                    return ITEM_LAYOUT_NINETEEN;
                case ITEM_TYPE_TWENTY:
                    return ITEM_LAYOUT_TWENTY;

                default:
                    return ITEM_LAYOUT_ONE;
            }
        }
        return ITEM_LAYOUT_ONE;
    }

    @Override
    public int getItemCount() {
        return mChannelModules.size();
    }

}
