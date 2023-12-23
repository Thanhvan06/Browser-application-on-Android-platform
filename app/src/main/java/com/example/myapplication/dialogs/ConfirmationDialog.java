package com.example.myapplication.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;
import com.example.myapplication.listeners.ConfirmationDialogListener;

public class ConfirmationDialog {
    public static void showConfirmationDialog(Context context, String title, String message, final ConfirmationDialogListener listener) {
        // Sử dụng LayoutInflater để nạp layout XML
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.custom_confirmation_dialog, null);

        // Tìm các thành phần trong layout
        TextView titleTextView = customView.findViewById(R.id.custom_dialog_title);
        TextView messageTextView = customView.findViewById(R.id.custom_dialog_content);
        TextView actionYES = customView.findViewById(R.id.custom_dialog_action_yes);
        TextView actionNO = customView.findViewById(R.id.custom_dialog_action_no);
        TextView close = customView.findViewById(R.id.custom_dialog_close);

        // Đặt nội dung cho tiêu đề và nội dung
        titleTextView.setText(title);
        messageTextView.setText(message);

        // Tạo AlertDialog.Builder và đặt layout của nó
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(customView);

        // Tạo và hiển thị dialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Xử lý sự kiện khi nút Xác nhận được nhấn
        actionYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfirm();
                }
                dialog.dismiss(); // Đóng dialog sau khi xác nhận
            }
        });

        // Xử lý sự kiện khi nút Hủy được nhấn
        actionNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Đóng dialog sau khi hủy
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
