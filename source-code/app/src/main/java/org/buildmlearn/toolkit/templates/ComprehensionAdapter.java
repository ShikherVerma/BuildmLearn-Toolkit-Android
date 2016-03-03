package org.buildmlearn.toolkit.templates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.buildmlearn.toolkit.R;
import org.buildmlearn.toolkit.activity.TemplateEditor;
import org.buildmlearn.toolkit.views.TextViewPlus;

import java.util.ArrayList;

/**
 * @brief Adapter for displaying Comprehension Template Editor data.
 * <p/>
 * Created by shikher on 02/03/16.
 */
public class ComprehensionAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<ComprehensionModel> dataList;
    private int expandedPostion = -1;

    public ComprehensionAdapter(Context context, ArrayList<ComprehensionModel> data) {
        this.context = context;
        this.dataList = data;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public ComprehensionModel getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //TODO: setup different views for comprehension and quiz questions.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater;
        mInflater = LayoutInflater.from(context);
        Holder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.quiz_item, parent, false);
            holder = new Holder();

            holder.question = (TextViewPlus) convertView.findViewById(R.id.question);
            holder.options = new ArrayList<>();
            holder.options.add((TextViewPlus) convertView.findViewById(R.id.answer1));
            holder.options.add((TextViewPlus) convertView.findViewById(R.id.answer2));
            holder.options.add((TextViewPlus) convertView.findViewById(R.id.answer3));
            holder.options.add((TextViewPlus) convertView.findViewById(R.id.answer4));
            holder.questionIcon = (ImageView) convertView.findViewById(R.id.question_icon);
            holder.quizOptionsBox = (LinearLayout) convertView.findViewById(R.id.quiz_options_box);
            holder.delete = (Button) convertView.findViewById(R.id.quiz_item_delete);
            holder.edit = (Button) convertView.findViewById(R.id.quiz_item_edit);

        } else {
            holder = (Holder) convertView.getTag();
        }

        ComprehensionModel data = getItem(position);
        holder.question.setText(data.getQuizModel().getQuestion());
        if (data.getQuizModel().isSelected()) {
            holder.questionIcon.setImageResource(R.drawable.collapse);
            holder.quizOptionsBox.setVisibility(View.VISIBLE);
        } else {
            holder.questionIcon.setImageResource(R.drawable.expand);
            holder.quizOptionsBox.setVisibility(View.GONE);
        }

        for (int i = 0; i < holder.options.size(); i++) {
            if (i < data.getQuizModel().getOptions().size()) {
                int ascii = 65 + i;
                holder.options.get(i).setText(Character.toString((char) ascii) + ")  " + data.getQuizModel().getOptions().get(i));
                holder.options.get(i).setVisibility(View.VISIBLE);
            } else {
                holder.options.get(i).setVisibility(View.GONE);
            }
        }

        holder.options.get(data.getQuizModel().getCorrectAnswer()).setCustomFont(context, "roboto_medium.ttf");
        holder.options.get(data.getQuizModel().getCorrectAnswer()).setTextColor(context.getResources().getColor(R.color.quiz_correct_answer));

        holder.questionIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandedPostion >= 0 && expandedPostion != position && getItem(expandedPostion) != null) {
                    getItem(expandedPostion).getQuizModel().setIsSelected(false);
                }
                if (getItem(position).getQuizModel().isSelected()) {
                    getItem(position).getQuizModel().setIsSelected(false);
                    expandedPostion = -1;
                } else {
                    getItem(position).getQuizModel().setIsSelected(true);
                    expandedPostion = position;
                }
                notifyDataSetChanged();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem(position, context);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .title(R.string.dialog_delete_title)
                        .content(R.string.dialog_delete_msg)
                        .positiveText(R.string.dialog_yes)
                        .negativeText(R.string.dialog_no)
                        .build();

                dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataList.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();

                        ((TemplateEditor) context).restoreSelectedView();
                        expandedPostion = -1;
                    }
                });

                dialog.show();
            }
        });
        convertView.setTag(holder);
        return convertView;
    }

    //TODO: different editing stuff for comprehension and quiz questions.
    private void editItem(final int position, final Context context) {
        ComprehensionModel data = getItem(position);

        boolean wrapInScrollView = true;
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.quiz_edit)
                .customView(R.layout.quiz_dialog_add_question, wrapInScrollView)
                .positiveText(R.string.quiz_add)
                .negativeText(R.string.quiz_delete)
                .build();

        final EditText question = (EditText) dialog.findViewById(R.id.quiz_question);
        final ArrayList<RadioButton> buttons = new ArrayList<>();
        final ArrayList<EditText> options = new ArrayList<>();
        options.add((EditText) dialog.findViewById(R.id.quiz_option_1));
        options.add((EditText) dialog.findViewById(R.id.quiz_option_2));
        options.add((EditText) dialog.findViewById(R.id.quiz_option_3));
        options.add((EditText) dialog.findViewById(R.id.quiz_option_4));
        buttons.add((RadioButton) dialog.findViewById(R.id.quiz_radio_1));
        buttons.add((RadioButton) dialog.findViewById(R.id.quiz_radio_2));
        buttons.add((RadioButton) dialog.findViewById(R.id.quiz_radio_3));
        buttons.add((RadioButton) dialog.findViewById(R.id.quiz_radio_4));

        for (int i = 0; i < data.getQuizModel().getOptions().size(); i++) {
            options.get(i).setText(data.getQuizModel().getOptions().get(i));
        }

        question.setText(data.getQuizModel().getQuestion());
        buttons.get(data.getQuizModel().getCorrectAnswer()).setChecked(true);

        for (final RadioButton button : buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkButton(buttons, options, button.getId(), context);
                }
            });
        }

        dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValidated = true;
                int checkedAns = getCheckedAnswer(buttons);
                if (checkedAns < 0) {
                    Toast.makeText(context, "Choose a correct option", Toast.LENGTH_SHORT).show();
                    isValidated = false;
                }
                if (question.getText().toString().equals("")) {

                    question.setError("Question is required");
                    isValidated = false;
                }

                int optionCount = 0;
                for (EditText option : options) {
                    if (!option.getText().toString().equals("")) {
                        optionCount++;
                    }
                }
                if (optionCount < 2) {
                    Toast.makeText(context, "Minimum two multiple answers are required.", Toast.LENGTH_SHORT).show();
                    isValidated = false;
                }

                if (isValidated) {
                    dialog.dismiss();
                    ArrayList<String> answerOptions = new ArrayList<String>();
                    int correctAnswer = 0;
                    for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).isChecked() && !options.get(i).getText().toString().equals("")) {
                            correctAnswer = answerOptions.size();
                            answerOptions.add(options.get(i).getText().toString());
                        } else if (!options.get(i).getText().toString().equals("")) {
                            answerOptions.add(options.get(i).getText().toString());
                        }
                    }
                    String questionText = question.getText().toString();
                    dataList.set(position, new ComprehensionModel(false, null, new QuizModel(questionText, answerOptions, correctAnswer)));
                    notifyDataSetChanged();
                }
            }
        });
        dialog.show();
    }

    private void checkButton(ArrayList<RadioButton> buttons, ArrayList<EditText> options, int id, Context context) {
        for (RadioButton button : buttons) {
            if (button.getId() == id) {
                int index = buttons.indexOf(button);
                if (options.get(index).getText().toString().equals("")) {
                    Toast.makeText(context, "Enter a valid option before marking it as answer", Toast.LENGTH_LONG).show();
                    button.setChecked(false);
                    return;
                } else {
                    button.setChecked(true);
                }
            } else {
                button.setChecked(false);
            }
        }
    }

    private int getCheckedAnswer(ArrayList<RadioButton> buttons) {
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isChecked()) {
                return i;
            }
        }
        return -1;
    }

    //TODO: create another holder class for comprehension, also rename this one.
    public class Holder {
        TextViewPlus question;
        ImageView questionIcon;
        ArrayList<TextViewPlus> options;
        LinearLayout quizOptionsBox;
        Button delete;
        Button edit;
    }
}
