package com.github.abdularis.civsample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.abdularis.civ.AvatarImageView;

/**
 * Created by abdularis on 28/03/18.
 */

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.ViewHolder> {


    class Person {
        String name;
        String status;
        int avatar;

        Person(String name, String status, int avatar) {
            this.name = name;
            this.status = status;
            this.avatar = avatar;
        }
    }


    Person persons[] = new Person[] {
            new Person("Alice", "online", R.drawable.avatar_1),
            new Person("Aris", "available", R.drawable.avatar_2),
            new Person("Ben", "offline", R.drawable.avatar_3),
            new Person("Bernie", "online", R.drawable.avatar_4),
            new Person("Jen", "available", R.drawable.avatar_5),
            new Person("Jennie", "offline", 0),
            new Person("Jessie J.", "busy", R.drawable.avatar_1),
            new Person("josh", "online", R.drawable.avatar_2),
            new Person("Michele", "available", 0),
            new Person("Miemie", "online", R.drawable.avatar_4),
            new Person("Han", "offline", R.drawable.avatar_5),
            new Person("Aice", "online", R.drawable.avatar_1),
            new Person("Anna", "available", R.drawable.avatar_2),
            new Person("Bernard", "offline", R.drawable.avatar_3),
            new Person("Smith", "online", R.drawable.avatar_4),
            new Person("Star", "available", R.drawable.avatar_5),
            new Person("Crystal", "offline", 0),
            new Person("Jeremy", "busy", R.drawable.avatar_1),
            new Person("Jordan", "online", R.drawable.avatar_2),
            new Person("Anne", "available", 0),
            new Person("Lucy", "online", R.drawable.avatar_4),
            new Person("Merry", "offline", R.drawable.avatar_5)
    };


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(persons[position]);
    }

    @Override
    public int getItemCount() {
        return persons.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AvatarImageView avatar;
        TextView name;
        TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.text_name);
            status = itemView.findViewById(R.id.text_stat);
        }

        void bind(Person p) {
            if (p.avatar == 0) {
                avatar.setState(AvatarImageView.SHOW_INITIAL);
                avatar.setText(p.name);
            } else {
                avatar.setState(AvatarImageView.SHOW_IMAGE);
                avatar.setImageResource(p.avatar);
            }

            name.setText(p.name);
            status.setText(p.status);
        }
    }

}
