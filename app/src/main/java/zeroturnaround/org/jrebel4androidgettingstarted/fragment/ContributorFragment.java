package zeroturnaround.org.jrebel4androidgettingstarted.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;



import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zeroturnaround.org.jrebel4androidgettingstarted.ContributorsApplication;
import zeroturnaround.org.jrebel4androidgettingstarted.R;
import zeroturnaround.org.jrebel4androidgettingstarted.imageloader.impl.ImageLoader;
import zeroturnaround.org.jrebel4androidgettingstarted.service.Contributor;
import zeroturnaround.org.jrebel4androidgettingstarted.service.ContributorsService;
import zeroturnaround.org.jrebel4androidgettingstarted.service.GitHubService;
import zeroturnaround.org.jrebel4androidgettingstarted.service.Repository;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContributorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContributorFragment extends Fragment implements ContributorsService.ContributorsListener {

    @InjectExtra Contributor contributor;

    @BindView(R.id.textview_bio) TextView bio_textview;
    @BindView(R.id.textview_company) TextView company_textview;
    @BindView(R.id.textview_email) TextView email_textview;
    @BindView(R.id.textview_name) TextView name_textview;
    @BindView(R.id.contributor_avatar) ImageView avatar_imageview;
    @BindView(R.id.textview_repos) TextView repos_textview;

    private ContributorsService contributorService;


    public ContributorFragment() {
        // Required empty public constructor
    }


    public static ContributorFragment newInstance() {
        ContributorFragment fragment = new ContributorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contributorService = ((ContributorsApplication) getActivity().getApplicationContext()).getContributorService();
        contributorService.addListener(this);
        Dart.inject(this, getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contributor, container, false);

        ButterKnife.bind(this, rootView);
        onContributorLoaded(contributor);
        contributorService.requestUser(contributor);

        ImageLoader imageLoader = new ImageLoader();
        imageLoader.loadImage(contributor.getAvatarUrl(), avatar_imageview);

        return  rootView;
    }

    @Override
    public void onContributorsLoaded(List<Contributor> contributors) {
    }

    @Override
    public void onContributorsLoadFailed(String message) {
    }

    @Override
    public void onContributorLoaded(Contributor contributor) {
        bio_textview.setText(contributor.getBio());
        email_textview.setText(contributor.getEmail());
        company_textview.setText(contributor.getCompany() + ", " + contributor.getLocation());
        name_textview.setText(contributor.getName());

        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
        gitHubService.getRepos(contributor.getReposUrl()).enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                repos_textview.setText("Found repos: " + response.body().size());
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {

            }
        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        contributorService.removeListener(this);
    }

}
