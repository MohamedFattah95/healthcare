package com.gp.health.ui.intro;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.gp.health.R;
import com.gp.health.databinding.FragmentIntroBinding;

import org.jetbrains.annotations.NotNull;

import static com.gp.health.ui.intro.IntroActivity.introModels;

public class IntroFragment extends Fragment {

    int position = -1;
    Context context;

    FragmentIntroBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("POSITION");
        context = getContext();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentIntroBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvTitle.setText(introModels.get(position).getTitle());

        if (introModels.get(position).getDescription() != null && introModels.get(position).getDescription().contains("</")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.tvDesc.setText(Html.fromHtml(introModels.get(position).getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                binding.tvDesc.setText(Html.fromHtml(introModels.get(position).getDescription()));
            }
        } else if (introModels.get(position).getDescription() != null) {
            binding.tvDesc.setText(introModels.get(position).getDescription());
        }


        if (introModels.get(position).getImage().contains("svg")) {
            GlideToVectorYou.justLoadImage((Activity) context,
                    Uri.parse(introModels.get(position).getImage()),
                    binding.ivIntro);
        } else {
            Glide.with(context)
                    .load(introModels.get(position).getImage())
                    .error(R.color.colorPrimary)
                    .placeholder(R.color.colorPrimary)
                    .into(binding.ivIntro);

        }

    }
}