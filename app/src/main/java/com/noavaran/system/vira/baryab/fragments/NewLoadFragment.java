package com.noavaran.system.vira.baryab.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.noavaran.system.vira.baryab.AppController;
import com.noavaran.system.vira.baryab.BaseFragment;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.DestinationActivity;
import com.noavaran.system.vira.baryab.activities.ImageCropperActivity;
import com.noavaran.system.vira.baryab.activities.MapsActivity;
import com.noavaran.system.vira.baryab.activities.RoutingActivity;
import com.noavaran.system.vira.baryab.customviews.CustomEditText;
import com.noavaran.system.vira.baryab.customviews.CustomSwitchButton;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.customviews.ExpandableLayout;
import com.noavaran.system.vira.baryab.database.models.LoadingFareType;
import com.noavaran.system.vira.baryab.database.models.TruckType;
import com.noavaran.system.vira.baryab.dialogs.MessageDialog;
import com.noavaran.system.vira.baryab.fragments.controllers.NewLoadController;
import com.noavaran.system.vira.baryab.fragments.delegates.NewLoadDelegate;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.ImagePicker;
import com.noavaran.system.vira.baryab.utils.ImageUtil;
import com.noavaran.system.vira.baryab.utils.PersianDateUtil;
import com.noavaran.system.vira.baryab.utils.PersianFormatHelper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

public class NewLoadFragment extends BaseFragment implements NewLoadDelegate.View {
    private View rootView;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    private ScrollView svContainer;

    private CustomTextView btnCarChoose;
    private CustomTextView tvCarChooseHint;

    private ExpandableLayout elCarLength;
    private CustomTextView btnCarLengthArrowDropDown;
    private CustomTextView tvCarLengthHeaderLabel;
    private CustomEditText etCarLengthFrom;
    private CustomEditText etCarLengthTo;

    private ExpandableLayout elCarWidth;
    private CustomTextView btnCarWidthArrowDropDown;
    private CustomTextView tvCarWidthHeaderLabel;
    private CustomEditText etCarWidthFrom;
    private CustomEditText etCarWidthTo;

    private ExpandableLayout elCarHeight;
    private CustomTextView btnCarHeightArrowDropDown;
    private CustomTextView tvCarHeightHeaderLabel;
    private CustomEditText etCarHeightFrom;
    private CustomEditText etCarHeightTo;

    private CustomEditText etLoadingType;
    private CustomEditText etLoadingWeight;

    private ExpandableLayout elLoadingFareType;
    private CustomTextView btnLoadingFareTypeArrowDropDown;
    private RecyclerView rvLoadingFareType;
    private CustomTextView tvLoadingFareTypeHint;
    private CustomEditText etLoadingFare;
    private CustomTextView btnLoadingTime;
    private CustomTextView tvLoadingTimeHint;
    private CustomEditText etّDescription;
    private CustomTextView btnChooseLoadingPhoto;
    private ImageView ivLoadingPhotoPreview;
    private ExpandableLayout elMoreDetails;
    private RelativeLayout rlMoreDetails;
    private CustomTextView btnMoreDetailsArrowDropDown;
    private CustomEditText etSpendingWarehouse;
    private CustomTextView tvDetermineDischargeTimeHint;
    private CustomTextView btnDetermineDischargeTime;
    private CustomTextView tvDetermineExpirationTimeHint;
    private CustomTextView btnDetermineExpirationTime;
    private CustomSwitchButton btnPaymentIsPaidAfterLoadingArriveToDestination;
    private CustomSwitchButton btnLoadingIsGoingRound;
    private CustomSwitchButton btnHavingIntactTent;
    private RelativeLayout btnSave;
    private NewLoadController controller;
    private boolean isBroadcastRequestCarTypeRegistered = false;
    private int rlMoreDetailsMeasuredHeight;

    private LinearLayout lytOrigin, lytDestinition;

    // This variable is used to save cropped image's path, after all data sent to server we need to
    // remove cropped image that has been chosen by user
    private String croppedImagePathTemp = "";


    private AppCompatCheckBox checkbox_map;
    private AppCompatImageView image_map_origin,
            image_map_destination;
    String map_value = "map_value";

    public NewLoadFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_new_load, container, false);
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

        this.lytOrigin = this.rootView.findViewById(R.id.lyt_origin);
        this.lytDestinition = this.rootView.findViewById(R.id.lyt_destinition);

        this.svContainer = this.rootView.findViewById(R.id.frNewLoad_svContainer);
        this.btnCarChoose = this.rootView.findViewById(R.id.frNewLoad_btnCarChoose);
        this.tvCarChooseHint = this.rootView.findViewById(R.id.frNewLoad_tvCarChooseHint);
        this.elCarLength = this.rootView.findViewById(R.id.frNewLoad_elCarLength);
        this.btnCarLengthArrowDropDown = this.rootView.findViewById(R.id.frNewLoad_btnCarLengthArrowDropDown);
        this.tvCarLengthHeaderLabel = this.rootView.findViewById(R.id.frNewLoad_tvCarLengthHeaderLabel);
        this.etCarLengthFrom = this.rootView.findViewById(R.id.frNewLoad_etCarLengthFrom);
        this.etCarLengthTo = this.rootView.findViewById(R.id.frNewLoad_etCarLengthTo);
        this.elCarWidth = this.rootView.findViewById(R.id.frNewLoad_elCarWidth);
        this.btnCarWidthArrowDropDown = this.rootView.findViewById(R.id.frNewLoad_btnCarWidthArrowDropDown);
        this.tvCarWidthHeaderLabel = this.rootView.findViewById(R.id.frNewLoad_tvCarWidthHeaderLabel);
        this.etCarWidthFrom = this.rootView.findViewById(R.id.frNewLoad_etCarWidthFrom);
        this.etCarWidthTo = this.rootView.findViewById(R.id.frNewLoad_etCarWidthTo);
        this.elCarHeight = this.rootView.findViewById(R.id.frNewLoad_elCarHeight);
        this.btnCarHeightArrowDropDown = this.rootView.findViewById(R.id.frNewLoad_btnCarHeightArrowDropDown);
        this.tvCarHeightHeaderLabel = this.rootView.findViewById(R.id.frNewLoad_tvCarHeightHeaderLabel);
        this.etCarHeightFrom = this.rootView.findViewById(R.id.frNewLoad_etCarHeightFrom);
        this.etCarHeightTo = this.rootView.findViewById(R.id.frNewLoad_etCarHeightTo);
        this.etLoadingType = this.rootView.findViewById(R.id.frNewLoad_etLoadingType);
        this.etLoadingWeight = this.rootView.findViewById(R.id.frNewLoad_etLoadingWeight);
        this.elLoadingFareType = this.rootView.findViewById(R.id.frNewLoad_elLoadingFareType);
        this.btnLoadingFareTypeArrowDropDown = this.rootView.findViewById(R.id.frNewLoad_btnLoadingFareTypeArrowDropDown);
        this.rvLoadingFareType = this.rootView.findViewById(R.id.frNewLoad_rvLoadingFareType);
        this.tvLoadingFareTypeHint = this.rootView.findViewById(R.id.frNewLoad_tvLoadingFareTypeHint);
        this.etLoadingFare = this.rootView.findViewById(R.id.frNewLoad_etLoadingFare);
        this.btnLoadingTime = this.rootView.findViewById(R.id.frNewLoad_btnLoadingTime);
        this.tvLoadingTimeHint = this.rootView.findViewById(R.id.frNewLoad_tvLoadingTimeHint);
        this.etّDescription = this.rootView.findViewById(R.id.frNewLoad_etّDescription);
        this.btnChooseLoadingPhoto = this.rootView.findViewById(R.id.frNewLoad_btnChooseLoadingPhoto);
        this.ivLoadingPhotoPreview = this.rootView.findViewById(R.id.frNewLoad_ivLoadingPhotoPreview);
        this.elMoreDetails = this.rootView.findViewById(R.id.frNewLoad_elMoreDetails);
        this.rlMoreDetails = this.rootView.findViewById(R.id.frNewLoad_rlMoreDetails);
        this.btnMoreDetailsArrowDropDown = this.rootView.findViewById(R.id.frNewLoad_btnMoreDetailsArrowDropDown);
        this.etSpendingWarehouse = this.rootView.findViewById(R.id.frNewLoad_etSpendingWarehouse);
        this.tvDetermineDischargeTimeHint = this.rootView.findViewById(R.id.frNewLoad_tvDetermineDischargeTimeHint);
        this.btnDetermineDischargeTime = this.rootView.findViewById(R.id.frNewLoad_btnDetermineDischargeTime);
        this.tvDetermineExpirationTimeHint = this.rootView.findViewById(R.id.frNewLoad_tvDetermineExpirationTimeHint);
        this.btnDetermineExpirationTime = this.rootView.findViewById(R.id.frNewLoad_btnDetermineExpirationTime);
        this.btnPaymentIsPaidAfterLoadingArriveToDestination = this.rootView.findViewById(R.id.frNewLoad_btnPaymentIsPaidAfterLoadingArriveToDestination);
        this.btnLoadingIsGoingRound = this.rootView.findViewById(R.id.frNewLoad_btnLoadingIsGoingRound);
        this.btnHavingIntactTent = this.rootView.findViewById(R.id.frNewLoad_btnHavingIntactTent);

        this.btnSave = this.rootView.findViewById(R.id.frNewLoad_btnSave);
    }

    private void initComponents() {
        controller = new NewLoadController(getActivity(), this);

        getEditTextLoadingTimeHint().setText(PersianDateUtil.getPersianDate(new Date().getTime()));
        getTextViewDetermineExpirationTimeHint().setText(PersianDateUtil.getPersianDate(new Date().getTime() + 3 * 24 * 60 * 60 * 1000));

        isBroadcastRequestCarTypeRegistered = true;
        getActivity().registerReceiver(controller.broadcastRequestCarType, new IntentFilter("com.noavaran.system.vira.baryab.REQUEST_CARType"));

        ImagePicker.setMinQuality(450, 450);

        rlMoreDetailsMeasuredHeight = rlMoreDetails.getLayoutParams().height;
    }

    private void setViewsListeners() {

        lytOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RoutingActivity.class));
            }
        });

        lytDestinition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RoutingActivity.class));
            }
        });


        this.btnCarChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TruckType.listAll(TruckType.class).size() > 0)
                    controller.getCarTypeChooserDialog().show(getFragmentManager(), "CarTypeChooserDialogTag");
                else {
                    MessageDialog messageDialog = new MessageDialog(getActivity(), getString(R.string.app_name), "سیستم در حال همگام سازی اطلاعات از سرور است، چنانچه اینترنت شما برقرار نیست لطفا آن را روشن نمایید.");
                    messageDialog.show();
                }
            }
        });

        this.elCarLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elCarWidth.isExpanded()) elCarWidth.toggleExpansion();
                if (elCarHeight.isExpanded()) elCarHeight.toggleExpansion();

                etCarLengthFrom.setFocusableInTouchMode(true);
                etCarLengthFrom.requestFocus();
                elCarLength.toggleExpansion();

                elCarLength.post(new Runnable() {
                    @Override
                    public void run() {
                        if (elCarLength.isExpanded())
                            rlMoreDetails.getLayoutParams().height = rlMoreDetailsMeasuredHeight;
                        else
                            rlMoreDetails.getLayoutParams().height = rlMoreDetailsMeasuredHeight + elCarLength.getLayoutParams().height;
                    }
                });
            }
        });

        this.elCarWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elCarLength.isExpanded()) elCarLength.toggleExpansion();
                if (elCarHeight.isExpanded()) elCarHeight.toggleExpansion();

                etCarWidthFrom.setFocusableInTouchMode(true);
                etCarWidthFrom.requestFocus();
                elCarWidth.toggleExpansion();

                elCarWidth.post(new Runnable() {
                    @Override
                    public void run() {
                        if (elCarWidth.isExpanded())
                            rlMoreDetails.getLayoutParams().height = rlMoreDetailsMeasuredHeight;
                        else
                            rlMoreDetails.getLayoutParams().height = rlMoreDetailsMeasuredHeight + elCarWidth.getLayoutParams().height;
                    }
                });
            }
        });

        this.elCarHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elCarLength.isExpanded()) elCarLength.toggleExpansion();
                if (elCarWidth.isExpanded()) elCarWidth.toggleExpansion();

                etCarHeightFrom.setFocusableInTouchMode(true);
                etCarHeightFrom.requestFocus();
                elCarHeight.toggleExpansion();

                elCarHeight.post(new Runnable() {
                    @Override
                    public void run() {
                        if (elCarHeight.isExpanded())
                            rlMoreDetails.getLayoutParams().height = rlMoreDetailsMeasuredHeight;
                        else
                            rlMoreDetails.getLayoutParams().height = rlMoreDetailsMeasuredHeight + elCarHeight.getLayoutParams().height;
                    }
                });

            }
        });

        this.elLoadingFareType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoadingFareType.listAll(TruckType.class).size() > 0) {
                    if (elMoreDetails.isExpanded()) elMoreDetails.toggleExpansion();
                    if (elCarLength.isExpanded()) elCarLength.toggleExpansion();
                    if (elCarWidth.isExpanded()) elCarWidth.toggleExpansion();
                    if (elCarHeight.isExpanded()) elCarHeight.toggleExpansion();
                    elLoadingFareType.toggleExpansion();
                } else {
                    MessageDialog messageDialog = new MessageDialog(getActivity(), getString(R.string.app_name), "سیستم در حال همگام سازی اطلاعات از سرور است، چنانچه اینترنت شما برقرار نیست لطفا آن را روشن نمایید.");
                    messageDialog.show();
                }
            }
        });

        this.btnLoadingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.showPersianDatePickerDialog(tvLoadingTimeHint);
            }
        });

        this.btnChooseLoadingPhoto.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ImagePicker.pickImage(getActivity(), "لطفا عکس محموله خود را مشخص نمایید");
            }
        });

        elMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elLoadingFareType.isExpanded()) elLoadingFareType.toggleExpansion();
                elMoreDetails.toggleExpansion();
            }
        });

        this.btnDetermineDischargeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.showPersianDatePickerDialog(tvDetermineDischargeTimeHint);
            }
        });

        this.btnDetermineExpirationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.showPersianDatePickerDialog(tvDetermineExpirationTimeHint);
            }
        });

        this.btnPaymentIsPaidAfterLoadingArriveToDestination.setOnCheckedChangeListener(new CustomSwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomSwitchButton view, boolean isChecked) {
                if (isChecked) {
                    controller.setRefundAfterArrival(true);
                } else {
                    controller.setRefundAfterArrival(false);
                }
            }
        });

        this.btnLoadingIsGoingRound.setOnCheckedChangeListener(new CustomSwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomSwitchButton view, boolean isChecked) {
                if (isChecked) {
                    controller.setShipmentGoingRound(true);
                } else {
                    controller.setShipmentGoingRound(false);
                }
            }
        });

        this.btnHavingIntactTent.setOnCheckedChangeListener(new CustomSwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomSwitchButton view, boolean isChecked) {
                if (isChecked) {
                    controller.setHavingWellTent(true);
                } else {
                    controller.setHavingWellTent(false);
                }
            }
        });

        this.btnSave.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                controller.save();
            }
        });

        etLoadingWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                etLoadingWeight.removeTextChangedListener(this);

                try {
                    String givenstring = s.toString();
                    Long longval;
                    if (givenstring.contains(",")) {
                        givenstring = givenstring.replaceAll(",", "");
                    }
                    longval = Long.parseLong(givenstring);
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String formattedString = formatter.format(longval);
                    etLoadingWeight.setText(formattedString);
                    etLoadingWeight.setSelection(etLoadingWeight.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                etLoadingWeight.addTextChangedListener(this);

            }
        });

        etLoadingFare.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                etLoadingFare.removeTextChangedListener(this);

                try {
                    String givenstring = s.toString();
                    Long longval;

                    if (givenstring.contains(",")) {
                        givenstring = givenstring.replaceAll(",", "");
                    } else if (givenstring.contains("٬")) {
                        givenstring = givenstring.replaceAll("٬", "");
                    }

                    longval = Long.parseLong(PersianFormatHelper.toPersianNumber(givenstring));
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String formattedString = formatter.format(longval);
                    etLoadingFare.setText(formattedString);
                    etLoadingFare.setSelection(etLoadingFare.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                etLoadingFare.addTextChangedListener(this);

            }
        });

        etSpendingWarehouse.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                etSpendingWarehouse.removeTextChangedListener(this);

                try {
                    String givenstring = s.toString();
                    Long longval;
                    if (givenstring.contains(",")) {
                        givenstring = givenstring.replaceAll(",", "");
                    }
                    longval = Long.parseLong(givenstring);
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String formattedString = formatter.format(longval);
                    etSpendingWarehouse.setText(formattedString);
                    etSpendingWarehouse.setSelection(etSpendingWarehouse.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                etSpendingWarehouse.addTextChangedListener(this);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == Configuration.REQUEST_CODE_CROP_IMAGE) {
            String path = data.getStringExtra(ImageCropperActivity.IMAGE_PATH);

            this.croppedImagePathTemp = path;

            if (path == null) {
                return;
            }

            Bitmap bitmap = ImageUtil.getInstance(getActivity()).getBitmapFromFilePath(path, "001.jpg", 450, 450);
            ivLoadingPhotoPreview.setVisibility(View.VISIBLE);
            ivLoadingPhotoPreview.setImageBitmap(bitmap);
        } else if (resultCode == getActivity().RESULT_OK && requestCode == ImagePicker.mPickImageRequestCode) {
            final Bitmap bitmap = ImagePicker.getImageFromResult(AppController.getContext(), requestCode, resultCode, data);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Uri tempUri = ImageUtil.getInstance(getActivity()).getImageUri(bitmap);
                    File finalFile = new File(ImageUtil.getInstance(getActivity()).getRealPathFromURI(tempUri));

                    controller.startCropImage(finalFile.getPath());
                }
            }, 500);
        } else if (resultCode == getActivity().RESULT_OK && requestCode == Configuration.REQUEST_CODE_PLACES_INFO) {
            try {
                if (data.hasExtra("place_info")) {
                    ((CustomTextView) this.rootView.findViewById(R.id.frNewLoad_tvLocationHint)).setTextColor(Color.parseColor("#64b5f6"));
                    controller.setPlacesInfo(data.getStringExtra("place_info"));
                } else {
                    ((CustomTextView) this.rootView.findViewById(R.id.frNewLoad_tvLocationHint)).setTextColor(Color.parseColor("#e57373"));
                    setLocationHint("خطا در ارتباط با سرور. لطفا بعدا مجددا تلاش نمایید.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void setCarChooserHint(String carTypeFullname) {
        try {
            tvCarChooseHint.setVisibility(View.VISIBLE);
            tvCarChooseHint.setText(carTypeFullname);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public CustomTextView getLocationHint() {
        return null;
    }

    @Override
    public ScrollView getSvContainer() {
        return svContainer;
    }

    @Override
    public void setLocationHint(String location) {

    }

    @Override
    public CustomTextView getCarChooseHint() {
        return tvCarChooseHint;
    }

    @Override
    public ExpandableLayout getCarLengthView() {
        return elCarLength;
    }

    @Override
    public ExpandableLayout getCarWidthView() {
        return elCarWidth;
    }

    @Override
    public ExpandableLayout getCarHeightView() {
        return elCarHeight;
    }

    @Override
    public CustomEditText getEditTextCarLengthFrom() {
        return etCarLengthFrom;
    }

    @Override
    public CustomEditText getEditTextCarLengthTo() {
        return etCarLengthTo;
    }

    @Override
    public CustomEditText getEditTextCarWidthFrom() {
        return etCarWidthFrom;
    }

    @Override
    public CustomEditText getEditTextCarWidthTo() {
        return etCarWidthTo;
    }

    @Override
    public CustomEditText getEditTextCarHeightFrom() {
        return etCarHeightFrom;
    }

    @Override
    public CustomEditText getEditTextCarHeightTo() {
        return etCarHeightTo;
    }

    @Override
    public CustomEditText getEditTextLoadingType() {
        return etLoadingType;
    }

    @Override
    public CustomEditText getEditTextLoadingWeight() {
        return etLoadingWeight;
    }

    @Override
    public CustomEditText getEditTextLoadingFare() {
        return etLoadingFare;
    }

    @Override
    public CustomTextView getEditTextLoadingTimeHint() {
        return tvLoadingTimeHint;
    }

    @Override
    public CustomEditText getEditTextDescription() {
        return etّDescription;
    }

    @Override
    public ImageView getImagViewLoadingPhotoPreview() {
        return ivLoadingPhotoPreview;
    }

    @Override
    public CustomEditText getEditTextSpendingWarehouse() {
        return etSpendingWarehouse;
    }

    @Override
    public CustomTextView getTextViewDetermineDischargeTimeHint() {
        return tvDetermineDischargeTimeHint;
    }

    @Override
    public CustomTextView getTextViewDetermineExpirationTimeHint() {
        return tvDetermineExpirationTimeHint;
    }

    @Override
    public CustomSwitchButton getSwithchButtonPaymentIsPaidAfterLoadingArriveToDestination() {
        return btnPaymentIsPaidAfterLoadingArriveToDestination;
    }

    @Override
    public CustomSwitchButton getSwithchButtonLoadingIsGoingRound() {
        return btnLoadingIsGoingRound;
    }

    @Override
    public CustomSwitchButton getSwithchButtonHavingIntactTent() {
        return btnHavingIntactTent;
    }

    @Override
    public void setCarLengthHeaderLabel(String text) {
        tvCarLengthHeaderLabel.setText(text);
    }

    @Override
    public void setCarWidthHeaderLabel(String text) {
        tvCarWidthHeaderLabel.setText(text);
    }

    @Override
    public void setCarHeightHeaderLabel(String text) {
        tvCarHeightHeaderLabel.setText(text);
    }

    @Override
    public void hideLoadingFareView() {
        rootView.findViewById(R.id.frNewLoad_tvLoadingFareIcon).setVisibility(View.GONE);
        rootView.findViewById(R.id.frNewLoad_tvLoadingFare).setVisibility(View.GONE);
        rootView.findViewById(R.id.frNewLoad_tvLoadingFareHint).setVisibility(View.GONE);
        rootView.findViewById(R.id.frNewLoad_etLoadingFare).setVisibility(View.GONE);
    }

    @Override
    public void showLoadingFareView() {
        rootView.findViewById(R.id.frNewLoad_tvLoadingFareIcon).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.frNewLoad_tvLoadingFare).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.frNewLoad_tvLoadingFareHint).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.frNewLoad_etLoadingFare).setVisibility(View.VISIBLE);

    }

    @Override
    public RecyclerView getLoadingFareTypeRecyclerView() {
        return rvLoadingFareType;
    }

    @Override
    public CustomTextView getTextViewLoadingFareTypeHint() {
        return tvLoadingFareTypeHint;
    }

    @Override
    public void collapseAllExpandableLayout() {
        if (elLoadingFareType.isExpanded()) elLoadingFareType.toggleExpansion();
        if (elMoreDetails.isExpanded()) elMoreDetails.toggleExpansion();
    }

    @Override
    public void removeCroppedImage() {
        File file = new File(croppedImagePathTemp);
        if (file.exists())
            deleteFileFromMediaStore(getActivity().getContentResolver(), file);
    }

    public static void deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
        String canonicalPath;

        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }

        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri, MediaStore.Files.FileColumns.DATA + "=?", new String[]{canonicalPath});

        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
            }
        }
    }

    private void gotoActivityMap() {
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("place_info", controller.getPlaceInfo());
        intent.putExtra("places", bundle);
        startActivityForResult(intent, Configuration.REQUEST_CODE_PLACES_INFO);
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

//        if (isRequestCarBroadcastRegistered) {
//            getActivity().unregisterReceiver(controller.broadcastRequestCarType);
//            isRequestCarBroadcastRegistered = false;
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (isBroadcastRequestCarTypeRegistered) {
            getActivity().unregisterReceiver(controller.broadcastRequestCarType);
            isBroadcastRequestCarTypeRegistered = false;
        }

        super.onDestroy();
    }
}