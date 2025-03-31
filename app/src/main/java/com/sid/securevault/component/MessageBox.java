package com.sid.securevault.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.sid.securevault.R;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageBox {

    private String title, salute, description, message;

    public static void showMessageBox(MessageBox model, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View messageBoxView = inflater.inflate(R.layout.popup_message_box, null);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MessageBox_Popup_Style);
        builder.setView(messageBoxView);
        AlertDialog dialog = builder.create();

        // Get references to the views in the custom layout
        TextView title = messageBoxView.findViewById(R.id.id_mb_title);
        TextView salute = messageBoxView.findViewById(R.id.id_mb_salute);
        TextView description = messageBoxView.findViewById(R.id.id_mb_description);
        TextView message = messageBoxView.findViewById(R.id.id_mb_message);
        Button button = messageBoxView.findViewById(R.id.id_mb_btn);

        // Customize the views
        title.setText(model.getTitle());
        salute.setText(model.getSalute());
        description.setText(model.getDescription());
        message.setText(model.getMessage());

        dialog.getWindow().getAttributes().windowAnimations = R.style.MessageBox_Popup_Style;
        button.setOnClickListener(_ -> dialog.dismiss());
        dialog.show();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
    }
}