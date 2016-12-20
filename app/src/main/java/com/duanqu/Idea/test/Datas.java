package com.duanqu.Idea.test;

import com.duanqu.Idea.bean.FriendsListBean;
import com.duanqu.Idea.bean.SuggestGridBean;
import com.duanqu.Idea.bean.TopAdBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/27.
 */
public class Datas {
    //URLS


    //    public static String UploadHeader = "http://10.0.2.2:8080/EAsy/uploadHeader";
//    public static String SchoolInfo = "http://10.0.2.2:8080/EAsy/schoolInfo";
//    public static String Notification = "http://10.0.2.2:8080/EAsy/notification";
//     public static String RegistUrl = "http://10.0.2.2:8080/videoshare-sso/rest/user/register";
//     public static String Getrelation = "http://10.0.2.2:8080/videoshare-sso/rest/relation/getrelation";
//     public static String Relationbuild = "http://10.0.2.2:8080/videoshare-sso/rest/relation/relationbuild";
//     public static String Nickname ="http://10.0.2.2:8080/videoshare-sso/rest/info/nickname";
    public static String GetContacts = "http://115.159.159.65:8080/videoshare-sso/rest/relation/getcontacts";
    public static String LoginUrl = "http://115.159.159.65:8080/videoshare-sso/rest/user/login";
    public static String RegistUrl = "http://115.159.159.65:8080/videoshare-sso/rest/user/register";
    public static String Getrelation = "http://115.159.159.65:8080/videoshare-sso/rest/relation/getrelation";
    public static String Relationbuild = "http://115.159.159.65:8080/videoshare-sso/rest/relation/relationbuild";
    public static String Nickname ="http://115.159.159.65:8080/videoshare-sso/rest/info/nickname";
    public static String detail = "http://115.159.159.65:8080/videoshare-sso/rest/info/detail";
    public static String imageUrl = "http://115.159.159.65:8080/videoshare-sso/rest/info/backgound";
    public static String GetImageUrl = "http://115.159.159.65:8080/videoshare-sso/rest/info/getParam";
    public static String HeadUrl = "http://115.159.159.65:8080/videoshare-sso/rest/info/userhead";
    public static String GetHeadUrl = "http://115.159.159.65:8080/videoshare-sso/rest/info/getParam";
    public static String PublishFeed = "http://115.159.27.70:8084/rest/feed/publish";
    public static String GetFeed = "http://localhost:8084/rest/feed/pullbyId";
    public static String GetUserInfoList = "http://115.159.159.65:8080/videoshare-sso/rest/info/userinfolist";
    public static String GetFriendFeeds = "http://115.159.27.70:8084/rest/feed/getFriendFeed";
    //public static String GetFriendsFeeds = "http://localhost:8084/rest/feed/getFriendFeed";
    public static String GetUserInfo = "http://115.159.159.65:8080/videoshare-sso/rest/info/userinfo";
    public static String PushComment = "http://115.159.27.70:3000/comment";
    public static String Pushattachcomment = "http://115.159.27.70:3000/attachcomment";

    //http://115.159.27.70:3000/comment?pageSize=5&pageNum=1&feedId=78&queryType=1
//    public static String GetLast = "http://10.0.2.2:8080/EAsy/Getlastblogs";
//    public static String GetCount = "http://10.0.2.2:8080/EAsy/getcount";
//    public static String GetBlogs = "http://10.0.2.2:8080/EAsy/Getblogs";
//      public static String Login = "http://10.0.2.2:8080/EAsy/AppLogin";
//    public static String GetReply = "http://10.0.2.2:8080/EAsy/getreply";
//    public static String SendReply = "http://10.0.2.2:8080/EAsy/sendReply";
//    public static String DeleteBlog = "http://10.0.2.2:8080/EAsy/delete";
//    public static String Operator = "http://10.0.2.2:8080/EAsy/opearator";
//    public static String Push = "http://10.0.2.2:8080/EAsy/getPush";








    public static SuggestGridBean suggestGridBean = new SuggestGridBean();
    public static SuggestGridBean suggestGridBean1 = new SuggestGridBean();
    public static SuggestGridBean suggestGridBean2 = new SuggestGridBean();
    public static SuggestGridBean suggestGridBean3 = new SuggestGridBean();
    public static SuggestGridBean suggestGridBean4 = new SuggestGridBean();
    public static SuggestGridBean suggestGridBean5 = new SuggestGridBean();
    public static SuggestGridBean suggestGridBean6 = new SuggestGridBean();
    public static SuggestGridBean suggestGridBean7 = new SuggestGridBean();
    public static SuggestGridBean suggestGridBean8 = new SuggestGridBean();
    public static SuggestGridBean suggestGridBean9 = new SuggestGridBean();
    public static FriendsListBean friendsListBean = new FriendsListBean();
    public static FriendsListBean friendsListBean1 = new FriendsListBean();
    public static TopAdBean temp = new TopAdBean();
    public static TopAdBean temp1 = new TopAdBean();
    public static TopAdBean temp2 = new TopAdBean();
    public static TopAdBean temp3 = new TopAdBean();

    public static String[] strings = new String[]{
      "123","456","789","456","789","456","789","456","789","456","789","456","789"
    };

    public static String username = "五颜六色";
    public static String ueser_bg = "http://115.159.159.65:8080/EAsy/user_bg.jpg";
    public static String ueser_bg1 = "http://115.159.159.65:8080/EAsy/psb.jpg";

    public static ArrayList<FriendsListBean> friends = new ArrayList<>();


    public static ArrayList<SuggestGridBean> suggestGridBeens = new ArrayList<>();
    public static ArrayList<SuggestGridBean> tjGridBeens = new ArrayList<>();
    public static String companyImage = "http://img4.imgtn.bdimg.com/it/u=2300271886,3193179484&fm=206&gp=0.jpg";
    public static String companyText = "我们就是这么放荡不羁";

    public static String userHead = "http://115.159.159.65:8080/EAsy/Headurl/3ebcfd6f-f456-429a-bf64-ca04f508.jpg";
    static {
        suggestGridBean.setCoverImage("http://115.159.159.65:8080/EAsy/ImageSrc/118cdd95-3a3b-457d-b5a8-963830a7.jpg");
        suggestGridBean.setTextInfo("深入人心的，沉痛演讲！");

        suggestGridBean1.setCoverImage("http://115.159.159.65:8080/EAsy/ImageSrc/8a0be53c-5d1d-40d8-bc07-d8cf906f.jpg");
        suggestGridBean1.setTextInfo("最新出炉的美女搞笑视频，快来看看吧！");

        suggestGridBean2.setCoverImage("http://115.159.159.65:8080/EAsy/ImageSrc/daf96513-8f36-4187-befa-1350574a.jpg");
        suggestGridBean2.setTextInfo("会当凌绝顶，一览众山小");

        suggestGridBean3.setCoverImage("http://115.159.159.65:8080/EAsy/ImageSrc/41391fb1-dad4-4e56-a8f2-83e4c326.jpg");
        suggestGridBean3.setTextInfo("作死小能手！");




        temp.setImage("http://115.159.159.65:8080/EAsy/ImageSrc/t1.png");
        temp.setText("测试用");
        temp1.setImage("http://115.159.159.65:8080/EAsy/ImageSrc/t2.png");
        temp1.setText("测试用");
        temp2.setImage("http://115.159.159.65:8080/EAsy/ImageSrc/t3.png");
        temp2.setText("测试用");
        temp3.setImage("http://115.159.159.65:8080/EAsy/ImageSrc/t4.png");
        temp3.setText("测试用");
        suggestGridBeens.add(suggestGridBean);
        suggestGridBeens.add(suggestGridBean);
        suggestGridBeens.add(suggestGridBean);
        suggestGridBeens.add(suggestGridBean);
        suggestGridBeens.add(suggestGridBean);
        suggestGridBeens.add(suggestGridBean);

        tjGridBeens.add(suggestGridBean2);
        tjGridBeens.add(suggestGridBean1);
//        tjGridBeens.add(suggestGridBean);
//        tjGridBeens.add(suggestGridBean3);


        friendsListBean.setUrl("http://115.159.159.65:8080/EAsy/Headurl/3ebcfd6f-f456-429a-bf64-ca04f508.jpg");
        friendsListBean.setName("五颜六色");
        friendsListBean.setUsername("E41414005");


        friendsListBean1.setUrl("http://115.159.159.65:8080/EAsy/Headurl/f6aa7245-5d3c-4887-8e78-8482fb28.jpg");
        friendsListBean1.setName("动力小车@何叔平");
        friendsListBean1.setUsername("E41414006");
        friends.add(friendsListBean1);
        friends.add(friendsListBean);

    }



}
