package cn.ck.security.business.security.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.ck.security.R;
import cn.ck.security.utils.DensityUtil;

/**
 * @author chengkun
 * @since 2019/1/16 22:53
 */
public class ItemLineDecoration extends RecyclerView.ItemDecoration {
    private int mydevider;
    private Paint dividerPaint;
    private Context mContext;
    private Bitmap mBitmap;
    private Rect mRect;

    public ItemLineDecoration(Context context) {
        mContext = context;
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_divider);
        dividerPaint = new Paint();
        //设置分割线颜色
        dividerPaint.setColor(context.getResources().getColor(R.color.colorAccent));
        //设置分割线宽度
        mydevider = DensityUtil.dp2px(mContext, 20);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mydevider;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft() + DensityUtil.dp2px(mContext, 12);
        int right = parent.getWidth() - parent.getPaddingRight() - DensityUtil.dp2px(mContext, 12);

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            int top = view.getBottom();
            int bottom = view.getBottom() + mydevider;
            //c.drawRect(left, top, right, bottom, dividerPaint);
            // c.drawBitmap(mBitmap, left, top, dividerPaint);
            c.drawBitmap(mBitmap, null, new Rect(left, top, right, bottom), dividerPaint);
        }
    }
}
