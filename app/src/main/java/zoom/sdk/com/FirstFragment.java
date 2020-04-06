package zoom.sdk.com;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.ZoomSDK;

public class FirstFragment extends Fragment  implements InitAuthSDKCallback, MeetingServiceListener {
    private View view;
    private Button joinMeeting;
    private AutoCompleteTextView idMeeting;
    private AutoCompleteTextView idDisplayName;
    private ZoomSDK mZoomSDK;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);
        joinMeeting = view.findViewById(R.id.joinMeeting);
        idMeeting = view.findViewById(R.id.idMeeting);
        idDisplayName = view.findViewById(R.id.idDisplayName);

        joinMeeting.setOnClickListener(new AwesomeButtonClick());

        mZoomSDK = ZoomSDK.getInstance();
        if (mZoomSDK.isLoggedIn()) {
//            finish();

        }

        InitAuthSDKHelper.getInstance().initSDK(getContext(), this);
        if (mZoomSDK.isInitialized()) {

            ZoomSDK.getInstance().getMeetingService().addListener(this);
            ZoomSDK.getInstance().getMeetingSettingsHelper().enable720p(true);
        }

        return view;
    }


    class AwesomeButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onClickJoin();
        }
    }


    private void onClickJoin() {
        if(!mZoomSDK.isInitialized())
        {
            Toast.makeText(getContext(),"ZoomSDK has not been initialized successfully",Toast.LENGTH_SHORT).show();
            InitAuthSDKHelper.getInstance().initSDK(getContext(), this);
            return;
        }

        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            ZoomSDK.getInstance().getSmsService().enableZoomAuthRealNameMeetingUIShown(false);
        } else {
            ZoomSDK.getInstance().getSmsService().enableZoomAuthRealNameMeetingUIShown(true);
        }

        String number = idMeeting.getText().toString();
        String name = idDisplayName.getText().toString();

        JoinMeetingParams params = new JoinMeetingParams();
        params.meetingNo = number;
        params.displayName = name;
        ZoomSDK.getInstance().getMeetingService().joinMeetingWithParams(getContext(), params);
    }



    @Override
    public void onZoomSDKInitializeResult(int i, int i1) {

    }

    @Override
    public void onZoomAuthIdentityExpired() {

    }

    @Override
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int i, int i1) {

    }


}

