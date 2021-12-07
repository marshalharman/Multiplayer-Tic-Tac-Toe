package androidsamples.java.tictactoe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OpenGamesAdapter extends RecyclerView.Adapter<OpenGamesAdapter.ViewHolder> {

  Context context;
  ArrayList<String> list;
  private OnGameClickListener mOnGameClickListener;

  public OpenGamesAdapter(Context context, ArrayList<String> list, OnGameClickListener onGameClickListener) {
    // FIXME if needed
    this.context = context;
    this.list = list;
    this.mOnGameClickListener = onGameClickListener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_item, parent, false);
    return new ViewHolder(view,mOnGameClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    // TODO bind the item at the given position to the holder
//    GameInfo user = list.get(position);
//    int user = list.get(position);
//    holder.mContentView.setText(user.getPlayer1());
    String player1 = list.get(position);

    if(player1 != null){
      holder.mContentView.setText(player1);
      holder.mIdView.setText(Integer.toString(position));
    }

  }

  @Override
  public int getItemCount() {
    return list.size(); // FIXME
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public final View mView;
    public final TextView mIdView;
    public final TextView mContentView;
    public OnGameClickListener onGameClickListener;

    public ViewHolder(View view, OnGameClickListener onGameClickListener) {
      super(view);
      mView = view;
      mIdView = view.findViewById(R.id.item_number);
      mContentView = view.findViewById(R.id.content);

//      view.setOnClickListener(this);
    }

    @NonNull
    @Override
    public String toString() {
      return super.toString() + " '" + mContentView.getText() + "'";
    }

    @Override
    public void onClick(View v) {
        onGameClickListener.onGameClick(getAbsoluteAdapterPosition());
    }
  }

  public interface OnGameClickListener{
    void onGameClick(int position);
  }
}