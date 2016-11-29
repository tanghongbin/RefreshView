package com.avoscloud.leanchatlib.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avoscloud.leanchatlib.action.ClickHeaderListener;
import com.avoscloud.leanchatlib.controller.AudioHelper;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.EmotionHelper;
import com.avoscloud.leanchatlib.controller.MessageHelper;
import com.avoscloud.leanchatlib.model.ConversationType;
import com.avoscloud.leanchatlib.model.UserInfo;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.avoscloud.leanchatlib.view.PlayButton;
import com.avoscloud.leanchatlib.view.RoundCornerImageView;
import com.avoscloud.leanchatlib.view.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.ocpsoft.prettytime.PrettyTime;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.avoscloud.leanchatlib.R.id;
import static com.avoscloud.leanchatlib.R.layout;

public class ChatMessageAdapter extends BaseAdapter {
  private static PrettyTime prettyTime = new PrettyTime();
  private ConversationType conversationType;
  private int msgViewTypes = 8;
  private ClickListener clickListener;
  private Context context;
  private List<AVIMTypedMessage> datas = new ArrayList<>();

  private String otherid ;

  private String otherPic ;

  private String uPic ;

    private ClickHeaderListener headerListener;

  public ChatMessageAdapter(Context context, ConversationType conversationType) {
    this.context = context;
    this.conversationType = conversationType;
  }

  public List<AVIMTypedMessage> getDatas() {
    return datas;
  }

  public void setDatas(List<AVIMTypedMessage> datas) {
    this.datas = datas;
  }

  // time
  public static String millisecsToDateString(long timestamp) {
    long gap = System.currentTimeMillis() - timestamp;
    if (gap < 1000 * 60 * 60 * 24) {
      String s = prettyTime.format(new Date(timestamp));
      //return s.replace(" ", "");
      return s;
    } else {
      SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
      return format.format(new Date(timestamp));
    }
  }

  public static boolean haveTimeGap(long lastTime, long time) {
    int gap = 1000 * 60 * 3;
    return time - lastTime > gap;
  }

  public void setClickListener(ClickListener clickListener) {
    this.clickListener = clickListener;
  }

    public void setOnClickHeaderListener(ClickHeaderListener listener){

        headerListener = listener;

    }

  @Override
  public int getItemViewType(int position) {
    AVIMTypedMessage msg = datas.get(position);
    boolean comeMsg = isComeMsg(msg);

    MsgViewType viewType;
    AVIMReservedMessageType msgType = AVIMReservedMessageType.getAVIMReservedMessageType(msg.getMessageType());
    switch (msgType) {
      case TextMessageType:
        viewType = comeMsg ? MsgViewType.ComeText : MsgViewType.ToText;
        break;
      case VideoMessageType:
      case ImageMessageType:
        viewType = comeMsg ? MsgViewType.ComeImage : MsgViewType.ToImage;
        break;
      case AudioMessageType:
        viewType = comeMsg ? MsgViewType.ComeAudio : MsgViewType.ToAudio;
        break;
      case LocationMessageType:
        viewType = comeMsg ? MsgViewType.ComeLocation : MsgViewType.ToLocation;
        break;

      default:
        throw new IllegalStateException();
    }
    return viewType.getValue();
  }

  @Override
  public int getViewTypeCount() {
    return msgViewTypes;
  }

  boolean isComeMsg(AVIMTypedMessage msg) {
    return !MessageHelper.fromMe(msg);
  }

  @Override
  public int getCount() {
    return datas.size();
  }

  @Override
  public Object getItem(int i) {
    return datas.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  public View getView(int position, View conView, ViewGroup parent) {
    AVIMTypedMessage msg = datas.get(position);
    final boolean isComMsg = isComeMsg(msg);
    if (conView == null) {
      try {
        conView = createViewByType(AVIMReservedMessageType.getAVIMReservedMessageType(msg.getMessageType()), isComMsg);
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    TextView sendTimeView = ViewHolder.findViewById(conView, id.sendTimeView);
    TextView contentView = ViewHolder.findViewById(conView, id.textContent);
    View contentLayout = ViewHolder.findViewById(conView, id.contentLayout);
    ImageView imageView = ViewHolder.findViewById(conView, id.imageView);
    PlayButton playBtn = ViewHolder.findViewById(conView, id.playBtn);
    TextView locationView = ViewHolder.findViewById(conView, id.locationView);
    TextView usernameView = ViewHolder.findViewById(conView, id.username);
    RoundCornerImageView avatarView = ViewHolder.findViewById(conView,id.avatar) ;
    avatarView.setAngie(12f,12f);
    View statusSendFailed = ViewHolder.findViewById(conView, id.status_send_failed);
    View statusSendSucceed = ViewHolder.findViewById(conView, id.status_send_succeed);
    View statusSendStart = ViewHolder.findViewById(conView, id.status_send_start);

    // timestamp
    if (position == 0 || haveTimeGap(datas.get(position - 1).getTimestamp(),
        msg.getTimestamp())) {
      sendTimeView.setVisibility(View.VISIBLE);
      sendTimeView.setText(millisecsToDateString(msg.getTimestamp()));

    } else {
      sendTimeView.setVisibility(View.GONE);
    }

      //
      avatarView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              if (isComMsg) {
                  headerListener.onClickHeaderListener("left");
              } else {
                  headerListener.onClickHeaderListener("right");
              }

          }
      });



    UserInfo user = ChatManager.getInstance().getChatManagerAdapter().getUserInfoById(msg.getFrom());
    if (user == null) {
      throw new NullPointerException("user is null");
    }
    if (isComMsg) {
      if (conversationType == null) {
        throw new NullPointerException("conv type is null");
      }
      if (conversationType == ConversationType.Single) {
        usernameView.setVisibility(View.GONE);
      } else {
        usernameView.setVisibility(View.VISIBLE);
        usernameView.setText(user.getUsername());
      }
    }
    //加载聊天时的头像

    ImageLoader.getInstance().displayImage(uPic, avatarView, PhotoUtils.avatarImageOptions);
    if(  isComMsg ){

      ImageLoader.getInstance().displayImage(otherPic, avatarView, PhotoUtils.avatarImageOptions);

    }
    //avatarView.setVisibility(View.INVISIBLE);



    AVIMReservedMessageType type = AVIMReservedMessageType.getAVIMReservedMessageType(msg.getMessageType());
    switch (type) {
      case TextMessageType:
        AVIMTextMessage textMsg = (AVIMTextMessage) msg;
        contentView.setText(EmotionHelper.replace(ChatManager.getContext(), textMsg.getText()));
        contentView.setTextSize(16);
        contentLayout.requestLayout();
        break;
      case ImageMessageType:
        AVIMImageMessage imageMsg = (AVIMImageMessage) msg;
        PhotoUtils.displayImageCacheElseNetwork(imageView, MessageHelper.getFilePath(imageMsg),
            imageMsg.getFileUrl());
        setImageOnClickListener(imageView, imageMsg);
        break;
      case AudioMessageType:
        initPlayBtn(msg, playBtn);
        break;
      case LocationMessageType:
        setLocationView(msg, locationView);
        break;
      default:
        break;
    }
    if (isComMsg == false) {
      hideStatusViews(statusSendStart, statusSendFailed, statusSendSucceed);
      setSendFailedBtnListener(statusSendFailed, msg);
      switch (msg.getMessageStatus()) {
        case AVIMMessageStatusFailed:
          statusSendFailed.setVisibility(View.VISIBLE);
          break;
        case AVIMMessageStatusSent:
          if (conversationType == ConversationType.Single) {
//            statusSendSucceed.setVisibility(View.VISIBLE);
          }
          break;
        case AVIMMessageStatusSending:
          statusSendStart.setVisibility(View.VISIBLE);
          break;
        case AVIMMessageStatusNone:
        case AVIMMessageStatusReceipt:
          break;
      }
    }
    return conView;
  }

  private void setSendFailedBtnListener(View statusSendFailed, final AVIMTypedMessage msg) {
    statusSendFailed.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (clickListener != null) {
          clickListener.onFailButtonClick(msg);
        }
      }
    });
  }

  private void hideStatusViews(View statusSendStart, View statusSendFailed, View statusSendSucceed) {
    statusSendFailed.setVisibility(View.GONE);
    statusSendStart.setVisibility(View.GONE);
    statusSendSucceed.setVisibility(View.GONE);
  }

  public void setLocationView(AVIMTypedMessage msg, TextView locationView) {
    final AVIMLocationMessage locMsg = (AVIMLocationMessage) msg;
    locationView.setText(locMsg.getText());
    locationView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        if (clickListener != null) {
          clickListener.onLocationViewClick(locMsg);
        }
      }
    });
  }

  private void initPlayBtn(AVIMTypedMessage msg, PlayButton playBtn) {
    playBtn.setLeftSide(isComeMsg(msg));
    AudioHelper audioHelper = AudioHelper.getInstance();
    playBtn.setAudioHelper(audioHelper);
    playBtn.setPath(MessageHelper.getFilePath(msg));
  }

  private void setImageOnClickListener(ImageView imageView, final AVIMImageMessage imageMsg) {
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (clickListener != null) {
          clickListener.onImageViewClick(imageMsg);
        }
      }
    });
  }

  public View createViewByType(AVIMReservedMessageType type, boolean comeMsg) throws InvocationTargetException {
    View baseView;
    if (comeMsg) {
      baseView = View.inflate(context, layout.chat_item_base_left, null);
    } else {
      baseView = View.inflate(context, layout.chat_item_base_right, null);
    }
    LinearLayout contentView = (LinearLayout) baseView.findViewById(id.contentLayout);
    int contentId;
    switch (type) {
      case TextMessageType:
        contentId = layout.chat_item_text;
        break;
      case AudioMessageType:
        contentId = layout.chat_item_audio;
        break;
      case VideoMessageType:
      case ImageMessageType:
        contentId = layout.chat_item_image;
        break;
      case LocationMessageType:
        contentId = layout.chat_item_location;
        break;
      default:
        throw new IllegalStateException();
    }
    contentView.removeAllViews();
    View content = View.inflate(context, contentId, null);
    if (type == AVIMReservedMessageType.AudioMessageType) {
      PlayButton btn = (PlayButton) content;
      btn.setLeftSide(comeMsg);
    } else if (type == AVIMReservedMessageType.TextMessageType) {
      TextView textView = (TextView) content;
      if (comeMsg) {
        textView.setTextColor(Color.BLACK);
      } else {
        textView.setTextColor(Color.BLACK);
      }
    }
    contentView.addView(content);
    return baseView;

  }

  public void add(AVIMTypedMessage message) {
    datas.add(message);
    notifyDataSetChanged();
  }

  private enum MsgViewType {
    ComeText(0), ToText(1), ComeImage(2), ToImage(3), ComeAudio(4), ToAudio(5), ComeLocation(6), ToLocation(7);
    int value;

    MsgViewType(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  public interface ClickListener {
    void onFailButtonClick(AVIMTypedMessage msg);

    void onLocationViewClick(AVIMLocationMessage locMsg);

    void onImageViewClick(AVIMImageMessage imageMsg);
  }

  public String getOtherid() {
    return otherid;
  }

  public void setOtherid(String otherid) {
    this.otherid = otherid;
  }

  public String getOtherPic() {
    return otherPic;
  }

  public void setOtherPic(String otherPic) {
    this.otherPic = otherPic;
  }

  public String getuPic() {
    return uPic;
  }

  public void setuPic(String uPic) {
    this.uPic = uPic;
  }
}
