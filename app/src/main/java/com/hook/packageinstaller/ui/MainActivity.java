package com.hook.packageinstaller.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hook.packageinstaller.BuildConfig;
import com.hook.packageinstaller.R;
import com.hook.packageinstaller.config.ConfigKeys;
import com.hook.packageinstaller.core.RestartScopeController;

public final class MainActivity extends Activity {
    private static final int TEXT = Color.rgb(23, 32, 42);
    private static final int MUTED = Color.rgb(105, 115, 133);
    private static final int ACCENT = Color.rgb(47, 111, 237);
    private static final int CARD = 0xF0FFFFFF;
    private static final int DIVIDER = 0x1017202A;

    private static final String[] KEYS = new String[]{
            ConfigKeys.AD_CLEAN,
            ConfigKeys.STORE_CLEAN,
            ConfigKeys.AUTH_BYPASS,
            ConfigKeys.RISK_BYPASS,
            ConfigKeys.RISK_UI_CLEAN,
            ConfigKeys.PURE_MODE_BYPASS,
            ConfigKeys.UNKNOWN_SOURCE_BYPASS,
            ConfigKeys.ANONYMOUS_SOURCE_BYPASS,
            ConfigKeys.PROMO_UI_CLEAN,
            ConfigKeys.ORIENTATION_LOCK_FIX
    };

    private static final int[] TITLES = new int[]{
            R.string.feature_ad,
            R.string.feature_store,
            R.string.feature_auth,
            R.string.feature_risk,
            R.string.feature_risk_ui,
            R.string.feature_pure,
            R.string.feature_unknown,
            R.string.feature_anonymous,
            R.string.feature_promo,
            R.string.feature_orientation
    };

    private static final int[] DESCRIPTIONS = new int[]{
            R.string.feature_ad_desc,
            R.string.feature_store_desc,
            R.string.feature_auth_desc,
            R.string.feature_risk_desc,
            R.string.feature_risk_ui_desc,
            R.string.feature_pure_desc,
            R.string.feature_unknown_desc,
            R.string.feature_anonymous_desc,
            R.string.feature_promo_desc,
            R.string.feature_orientation_desc
    };

    private static final String[][] ADAPTED_INSTALLERS = new String[][]{
            {"11.5.1.302", "1105001302"},
            {"12.7.1.300", "1207001300"},
            {"13.2.1.302", "1302001302"},
            {"13.3.1.302", "1303001302"},
            {"15.2.1.300", "1502001300"}
    };

    private SharedPreferences preferences;
    private Switch hideIconSwitch;
    private boolean suppressHideIconCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(ConfigKeys.PREFS, MODE_PRIVATE);
        configureWindow();
        setContentView(buildContentView());
    }

    private void configureWindow() {
        Window window = getWindow();
        int barColor = getResources().getColor(R.color.status_bar, getTheme());
        window.setStatusBarColor(barColor);
        window.setNavigationBarColor(barColor);
        int flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        if (Build.VERSION.SDK_INT >= 26) {
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
        window.getDecorView().setSystemUiVisibility(flags);
    }

    private View buildContentView() {
        FrameLayout frame = new FrameLayout(this);
        frame.setBackgroundResource(R.drawable.bg_screen);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setClipToPadding(false);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        int side = dp(20);
        content.setPadding(side, dp(18), side, dp(30));
        scrollView.addView(content, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        frame.addView(scrollView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        content.addView(buildHeader());
        content.addView(buildStatusCard());
        content.addView(buildFeatureCard());
        content.addView(buildAllButtons());
        content.addView(buildOtherCard());
        content.addView(buildFooter());
        return frame;
    }

    private View buildHeader() {
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);

        LinearLayout titleRow = new LinearLayout(this);
        titleRow.setOrientation(LinearLayout.HORIZONTAL);
        titleRow.setGravity(Gravity.BOTTOM);

        TextView title = text(getString(R.string.settings_title), 30, TEXT, Typeface.BOLD);
        title.setLetterSpacing(-0.02f);
        titleRow.addView(title, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView author = pill(getString(R.string.author_format,
                getString(R.string.author_name)));
        LinearLayout.LayoutParams authorParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        authorParams.bottomMargin = dp(4);
        titleRow.addView(author, authorParams);
        header.addView(titleRow);

        TextView subtitle = text(getString(R.string.module_subtitle), 13, MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams subtitleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        subtitleParams.topMargin = dp(5);
        header.addView(subtitle, subtitleParams);
        return header;
    }

    private View buildStatusCard() {
        LinearLayout card = card();
        card.setPadding(dp(20), dp(18), dp(20), dp(18));

        TextView label = text(getString(R.string.current_installer), 12, MUTED, Typeface.BOLD);
        label.setLetterSpacing(0.04f);
        card.addView(label);

        TextView version = text(installerVersionText(), 19, TEXT, Typeface.BOLD);
        LinearLayout.LayoutParams versionParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        versionParams.topMargin = dp(5);
        card.addView(version, versionParams);

        if (!isCurrentInstallerAdapted()) {
            TextView unsupported = text("当前安装器版本未适配", 12,
                    Color.rgb(180, 35, 24), Typeface.BOLD);
            LinearLayout.LayoutParams unsupportedParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            unsupportedParams.topMargin = dp(6);
            card.addView(unsupported, unsupportedParams);
        }

        TextView scope = text(getString(R.string.scope_label) + "  "
                + getString(R.string.scope_value), 12, MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams scopeParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scopeParams.topMargin = dp(7);
        card.addView(scope, scopeParams);

        Button restart = primaryButton(getString(R.string.restart_scope));
        restart.setOnClickListener(v -> {
            RestartScopeController.restart(this);
            Toast.makeText(this, R.string.restart_sent, Toast.LENGTH_SHORT).show();
        });
        LinearLayout.LayoutParams restartParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        restartParams.topMargin = dp(16);
        card.addView(restart, restartParams);

        TextView hint = text(getString(R.string.restart_scope_desc), 12, MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams hintParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        hintParams.topMargin = dp(8);
        card.addView(hint, hintParams);
        return card;
    }

    private View buildFeatureCard() {
        LinearLayout card = card();
        card.addView(sectionHeader(R.string.section_features, R.string.section_features_desc));

        for (int i = 0; i < KEYS.length; i++) {
            card.addView(createFeatureRow(KEYS[i], TITLES[i], DESCRIPTIONS[i]));
            if (i != KEYS.length - 1) {
                card.addView(divider());
            }
        }
        return card;
    }

    private View createFeatureRow(final String key, int titleRes, int descriptionRes) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(20), dp(13), dp(14), dp(13));

        LinearLayout textColumn = new LinearLayout(this);
        textColumn.setOrientation(LinearLayout.VERTICAL);
        TextView title = text(getString(titleRes), 15, TEXT, Typeface.BOLD);
        TextView description = text(getString(descriptionRes), 12, MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams descriptionParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        descriptionParams.topMargin = dp(3);
        textColumn.addView(title);
        textColumn.addView(description, descriptionParams);
        row.addView(textColumn, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        Switch toggle = styledSwitch();
        toggle.setChecked(preferences.getBoolean(key, true));
        toggle.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferences.edit().putBoolean(key, isChecked).apply());
        row.addView(toggle, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return row;
    }

    private View buildAllButtons() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowParams.topMargin = dp(12);
        row.setLayoutParams(rowParams);

        Button allOn = secondaryButton(getString(R.string.all_on));
        allOn.setOnClickListener(v -> setAll(true));
        LinearLayout.LayoutParams onParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        onParams.rightMargin = dp(6);
        row.addView(allOn, onParams);

        Button allOff = secondaryButton(getString(R.string.all_off));
        allOff.setOnClickListener(v -> setAll(false));
        LinearLayout.LayoutParams offParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        offParams.leftMargin = dp(6);
        row.addView(allOff, offParams);
        return row;
    }

    private View buildOtherCard() {
        LinearLayout card = card();
        card.addView(sectionHeader(R.string.section_other));

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(20), dp(13), dp(14), dp(13));

        LinearLayout textColumn = new LinearLayout(this);
        textColumn.setOrientation(LinearLayout.VERTICAL);
        TextView title = text(getString(R.string.hide_launcher), 15, TEXT, Typeface.BOLD);
        TextView description = text(getString(R.string.hide_launcher_desc), 12, MUTED,
                Typeface.NORMAL);
        LinearLayout.LayoutParams descriptionParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        descriptionParams.topMargin = dp(3);
        textColumn.addView(title);
        textColumn.addView(description, descriptionParams);
        row.addView(textColumn, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        hideIconSwitch = styledSwitch();
        hideIconSwitch.setChecked(LauncherIconController.isHidden(this));
        hideIconSwitch.setOnCheckedChangeListener(this::onHideIconChanged);
        row.addView(hideIconSwitch, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        card.addView(row);
        return card;
    }

    private View buildFooter() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);

        TextView footer = text(getString(R.string.footer), 11, MUTED, Typeface.NORMAL);
        row.addView(footer);

        TextView about = text("关于模块", 11, ACCENT, Typeface.BOLD);
        about.setGravity(Gravity.CENTER);
        about.setPadding(dp(10), dp(5), dp(10), dp(5));
        about.setBackground(ripple(rounded(0x142F6FED, 99), 0x222F6FED, 99));
        about.setOnClickListener(v -> showAboutDialog());
        LinearLayout.LayoutParams aboutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        aboutParams.leftMargin = dp(8);
        row.addView(about, aboutParams);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = dp(20);
        params.bottomMargin = dp(4);
        row.setLayoutParams(params);
        return row;
    }

    private void showAboutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        GradientDrawable background = rounded(0xF0FFFFFF, 28);
        background.setStroke(dp(1), 0x1AFFFFFF);
        card.setBackground(background);
        card.setClipToOutline(true);
        card.setPadding(dp(24), dp(22), dp(24), dp(20));

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_launcher);
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        icon.setPadding(dp(7), dp(7), dp(7), dp(7));
        icon.setBackground(rounded(0x142F6FED, 14));
        header.addView(icon, new LinearLayout.LayoutParams(dp(46), dp(46)));

        LinearLayout titleColumn = new LinearLayout(this);
        titleColumn.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams titleColumnParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        titleColumnParams.leftMargin = dp(12);
        TextView dialogTitle = text("关于模块", 20, TEXT, Typeface.BOLD);
        TextView dialogSubtitle = text("鸿蒙应用安装器净化", 12, MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams dialogSubtitleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogSubtitleParams.topMargin = dp(3);
        titleColumn.addView(dialogTitle);
        titleColumn.addView(dialogSubtitle, dialogSubtitleParams);
        header.addView(titleColumn, titleColumnParams);

        TextView close = text("×", 22, MUTED, Typeface.NORMAL);
        close.setGravity(Gravity.CENTER);
        close.setPadding(dp(8), 0, 0, 0);
        close.setOnClickListener(v -> dialog.dismiss());
        header.addView(close, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(46)));
        card.addView(header);

        LinearLayout versionRow = new LinearLayout(this);
        versionRow.setOrientation(LinearLayout.HORIZONTAL);
        versionRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams versionRowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        versionRowParams.topMargin = dp(16);
        versionRow.setLayoutParams(versionRowParams);
        versionRow.addView(pill("模块 v" + BuildConfig.VERSION_NAME));
        TextView adaptedTag = text("已适配 " + ADAPTED_INSTALLERS.length + " 个安装器版本",
                12, ACCENT, Typeface.BOLD);
        adaptedTag.setGravity(Gravity.CENTER);
        adaptedTag.setPadding(dp(12), dp(6), dp(12), dp(6));
        adaptedTag.setBackground(rounded(0x0F2F6FED, 99));
        LinearLayout.LayoutParams adaptedTagParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        adaptedTagParams.leftMargin = dp(8);
        versionRow.addView(adaptedTag, adaptedTagParams);
        card.addView(versionRow);

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setBackground(rounded(0x0D2F6FED, 18));
        info.setPadding(dp(14), dp(6), dp(14), dp(6));
        addAboutInfoRow(info, "作者", "MuxMu");
        addAboutInfoRow(info, "构建时间",
                BuildConfig.BUILD_TIME == null || BuildConfig.BUILD_TIME.length() == 0
                        ? "未知" : BuildConfig.BUILD_TIME);
        addAboutInfoRow(info, "QQ 群", "1124689174");
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        infoParams.topMargin = dp(16);
        card.addView(info, infoParams);

        TextView listTitle = text("已适配应用安装器", 14, TEXT, Typeface.BOLD);
        LinearLayout.LayoutParams listTitleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listTitleParams.topMargin = dp(18);
        card.addView(listTitle, listTitleParams);

        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        list.setBackground(rounded(0x0D2F6FED, 18));
        list.setPadding(dp(14), dp(6), dp(14), dp(6));
        for (int i = 0; i < ADAPTED_INSTALLERS.length; i++) {
            addAdaptedInstallerRow(list, ADAPTED_INSTALLERS[i][0],
                    ADAPTED_INSTALLERS[i][1]);
            if (i != ADAPTED_INSTALLERS.length - 1) {
                list.addView(innerDivider());
            }
        }
        LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listParams.topMargin = dp(8);
        card.addView(list, listParams);

        Button confirm = primaryButton("知道了");
        confirm.setOnClickListener(v -> dialog.dismiss());
        LinearLayout.LayoutParams confirmParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmParams.topMargin = dp(18);
        card.addView(confirm, confirmParams);

        dialog.setContentView(card);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setDimAmount(0.36f);
            window.setGravity(Gravity.CENTER);
        }
        dialog.show();
        if (window != null) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int width = Math.min((int) (screenWidth * 0.90f), dp(620));
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void addAboutInfoRow(LinearLayout parent, String label, String value) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(7), 0, dp(7));

        TextView labelView = text(label, 13, MUTED, Typeface.NORMAL);
        TextView valueView = text(value, 13, TEXT, Typeface.BOLD);
        row.addView(labelView, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(valueView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        parent.addView(row);
    }

    private void addAdaptedInstallerRow(LinearLayout parent,
                                        String versionName, String versionCode) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(8), 0, dp(8));

        TextView name = text(versionName, 12, TEXT, Typeface.BOLD);
        row.addView(name, new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView code = text(versionCode, 11, MUTED, Typeface.NORMAL);
        code.setGravity(Gravity.END);
        row.addView(code, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        parent.addView(row);
    }

    private View innerDivider() {
        View divider = new View(this);
        divider.setBackgroundColor(0x0A17202A);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(1)));
        return divider;
    }

    private void onHideIconChanged(CompoundButton buttonView, boolean isChecked) {
        if (suppressHideIconCallback) {
            return;
        }
        if (!isChecked) {
            LauncherIconController.setHidden(this, false);
            preferences.edit().putBoolean(ConfigKeys.HIDE_LAUNCHER_ICON, false).apply();
            Toast.makeText(this, R.string.hide_launcher_shown, Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.hide_launcher)
                .setMessage(R.string.hide_launcher_warning)
                .setPositiveButton(R.string.hide_launcher_confirm, (dialog, which) -> {
                    LauncherIconController.setHidden(this, true);
                    preferences.edit().putBoolean(ConfigKeys.HIDE_LAUNCHER_ICON, true).apply();
                    Toast.makeText(this, R.string.hide_launcher_hidden, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.cancel,
                        (dialog, which) -> revertHideIconSwitch())
                .setOnCancelListener(dialog -> revertHideIconSwitch())
                .show();
    }

    private void revertHideIconSwitch() {
        suppressHideIconCallback = true;
        hideIconSwitch.setChecked(false);
        suppressHideIconCallback = false;
    }

    private void setAll(boolean enabled) {
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : KEYS) {
            editor.putBoolean(key, enabled);
        }
        editor.apply();
        recreate();
    }

    private String installerVersionText() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.android.packageinstaller", 0);
            long code = Build.VERSION.SDK_INT >= 28
                    ? info.getLongVersionCode()
                    : info.versionCode;
            return (info.versionName == null ? "?" : info.versionName)
                    + "  ·  " + code;
        } catch (Throwable t) {
            return getString(R.string.installer_version_unknown);
        }
    }

    private boolean isCurrentInstallerAdapted() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.android.packageinstaller", 0);
            String versionName = info.versionName;
            String versionCode = String.valueOf(Build.VERSION.SDK_INT >= 28
                    ? info.getLongVersionCode()
                    : info.versionCode);
            for (String[] adapted : ADAPTED_INSTALLERS) {
                if (adapted[0].equals(versionName) && adapted[1].equals(versionCode)) {
                    return true;
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    private TextView pill(String value) {
        TextView pill = text(value, 12, ACCENT, Typeface.BOLD);
        pill.setGravity(Gravity.CENTER);
        pill.setPadding(dp(12), dp(6), dp(12), dp(6));
        pill.setBackground(rounded(0x142F6FED, 99));
        return pill;
    }

    private LinearLayout card() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        GradientDrawable background = rounded(CARD, 26);
        background.setStroke(dp(1), 0x12FFFFFF);
        card.setBackground(background);
        card.setClipToOutline(true);
        card.setElevation(dp(2));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = dp(14);
        card.setLayoutParams(params);
        return card;
    }

    private View sectionHeader(int titleRes) {
        return sectionHeader(titleRes, 0);
    }

    private View sectionHeader(int titleRes, int descriptionRes) {
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(dp(20), dp(18), dp(20), dp(9));

        TextView title = text(getString(titleRes), 18, TEXT, Typeface.BOLD);
        header.addView(title);
        if (descriptionRes != 0) {
            TextView description = text(getString(descriptionRes), 12, MUTED, Typeface.NORMAL);
            LinearLayout.LayoutParams descriptionParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            descriptionParams.topMargin = dp(3);
            header.addView(description, descriptionParams);
        }
        return header;
    }

    private View divider() {
        View divider = new View(this);
        divider.setBackgroundColor(DIVIDER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(1));
        params.leftMargin = dp(20);
        params.rightMargin = dp(20);
        divider.setLayoutParams(params);
        return divider;
    }

    private Switch styledSwitch() {
        Switch toggle = new Switch(this);
        toggle.setShowText(false);
        toggle.setText(null);
        toggle.setPadding(0, 0, 0, 0);
        toggle.setMinWidth(dp(48));
        toggle.setTrackTintList(new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}},
                new int[]{ACCENT, Color.rgb(213, 220, 232)}));
        toggle.setThumbTintList(ColorStateList.valueOf(Color.WHITE));
        return toggle;
    }

    private Button primaryButton(String value) {
        Button button = baseButton(value);
        button.setTextColor(Color.WHITE);
        button.setBackground(ripple(rounded(ACCENT, 16), 0x33FFFFFF, 16));
        return button;
    }

    private Button secondaryButton(String value) {
        Button button = baseButton(value);
        button.setTextColor(ACCENT);
        button.setBackground(ripple(rounded(0x142F6FED, 16), 0x222F6FED, 16));
        return button;
    }

    private Button baseButton(String value) {
        Button button = new Button(this);
        button.setText(value);
        button.setAllCaps(false);
        button.setTextSize(15);
        button.setTypeface(null, Typeface.BOLD);
        button.setGravity(Gravity.CENTER);
        button.setMinHeight(dp(50));
        button.setPadding(dp(18), 0, dp(18), 0);
        button.setStateListAnimator(null);
        button.setElevation(0f);
        return button;
    }

    private Drawable ripple(Drawable content, int rippleColor, int radiusDp) {
        return new RippleDrawable(ColorStateList.valueOf(rippleColor),
                content, rounded(0xFFFFFFFF, radiusDp));
    }

    private GradientDrawable rounded(int color, int radiusDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radiusDp));
        return drawable;
    }

    private TextView text(String value, float sizeSp, int color, int style) {
        TextView textView = new TextView(this);
        textView.setText(value);
        textView.setTextSize(sizeSp);
        textView.setTextColor(color);
        textView.setTypeface(Typeface.create("sans-serif", style));
        textView.setLineSpacing(0f, 1.05f);
        return textView;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
