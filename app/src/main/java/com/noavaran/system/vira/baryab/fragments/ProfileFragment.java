package com.noavaran.system.vira.baryab.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noavaran.system.vira.baryab.BaseFragment;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.SPreferences;
import com.noavaran.system.vira.baryab.activities.MainActivity;
import com.noavaran.system.vira.baryab.dialogs.ConfirmDialog;
import com.noavaran.system.vira.baryab.helpers.ActivitiesHelpers;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;

import org.json.JSONObject;

public class ProfileFragment extends BaseFragment {
    private View rootView;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    private LinearLayout btnMyProfile;
    private LinearLayout btnShare;
    private LinearLayout btnQuestions;
    private LinearLayout btnAboutUs;
    private LinearLayout btnCriticsSuggestions;
    private LinearLayout btnNotifications;
    private LinearLayout btnContactsUs;
    private LinearLayout btnLogout;

    public ProfileFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        this.inflater = inflater;
        this.savedInstanceState = savedInstanceState;

        return this.rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews();
        initComponents();
        setViewsListeners();
    }

    private void findViews() {
        btnMyProfile = (LinearLayout) this.rootView.findViewById(R.id.frProfile_btnMyProfile);
        btnShare = (LinearLayout) this.rootView.findViewById(R.id.frProfile_btnShare);
        btnQuestions = (LinearLayout) this.rootView.findViewById(R.id.frProfile_btnQuestions);
        btnAboutUs = (LinearLayout) this.rootView.findViewById(R.id.frProfile_btnAboutUs);
        btnCriticsSuggestions = (LinearLayout) this.rootView.findViewById(R.id.frProfile_btnCriticsSuggestions);
        btnNotifications = (LinearLayout) this.rootView.findViewById(R.id.frProfile_btnNotifications);
        btnContactsUs = (LinearLayout) this.rootView.findViewById(R.id.frProfile_btnContactsUs);
        btnLogout = (LinearLayout) this.rootView.findViewById(R.id.frProfile_btnLogout);
    }

    private void initComponents() {

    }

    private void setViewsListeners() {
        btnMyProfile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ActivitiesHelpers.getInstance(getActivity()).gotoActivityProfile();
            }
        });

        btnShare.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                doShare("سلام. برنامه باریاب نو نسخه متصدیان رو از کافه بازار دانلود کند. من نصبش کردم و باهاش کار کردم عالی بود فکر کنم بدرد بخوره.");
                doSharting();
            }
        });

        btnQuestions.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ActivitiesHelpers.getInstance(getActivity()).gotoActivityFAQ();
            }
        });

        btnAboutUs.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ActivitiesHelpers.getInstance(getActivity()).gotoActivityAboutUs();
            }
        });

        btnCriticsSuggestions.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ActivitiesHelpers.getInstance(getActivity()).gotoActivityComment();
            }
        });

        btnNotifications.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
            }
        });

        btnContactsUs.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ActivitiesHelpers.getInstance(getActivity()).gotoActivityContact();
            }
        });

        btnLogout.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ConfirmDialog confirmDialog = new ConfirmDialog(getActivity(), getString(R.string.app_name), "آیا مایل به خروج از برنامه خود می باشید؟\n\n در صورت تایید حساب کاربری و تمام اطلاعات شما از سیستم حذف خواهد شد.");
                confirmDialog.setOnClickListener(new ConfirmDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {

                        ((MainActivity) getActivity()).showDialogProgress("در حال حذف حساب کاربری شما از سیستم ...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((MainActivity) getActivity()).dismissDialogProgress();

                                SPreferences.getInstance(getActivity()).removeToken();
                                SPreferences.getInstance(getActivity()).removeOneSignalInfo();

                                ActivitiesHelpers.getInstance(getActivity()).gotoActivityLogin();
                            }
                        }, 2000);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                confirmDialog.show();
            }
        });
    }

    private void doSharting() {
        new OkHttpHelper(getActivity(), Configuration.API_SHARE_APPLICATION, OkHttpHelper.MEDIA_TYPE_JSON).doSharring(new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                ((MainActivity) getActivity()).showDialogProgress("در حال دریافت اطلاعات از سرور ...");
            }

            @Override
            public void onResponse(JSONObject result) {
                loadSharring(result);
            }

            @Override
            public void onRequestReject(String message) {
                ((MainActivity) getActivity()).dismissDialogProgress();
                ((MainActivity) getActivity()).showToastWarning(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                ((MainActivity) getActivity()).dismissDialogProgress();
                ((MainActivity) getActivity()).showToastError(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                ((MainActivity) getActivity()).dismissDialogProgress();
            }

            @Override
            public void onFinish() {
                ((MainActivity) getActivity()).dismissDialogProgress();
            }
        });
    }

    private void loadSharring(final JSONObject result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        String body = result.optJSONObject("data").optString("body");
                        String url = result.optJSONObject("data").optString("url");
                        doShare(body + "\n\n" + url);

                        ((MainActivity) getActivity()).dismissDialogProgress();
                    }
                } catch (Exception ex) {

                }
            }
        });
    }

    private void doShare(String shareBody) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "باریاب نو");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}