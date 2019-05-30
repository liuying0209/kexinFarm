package com.farm.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.core.constant.Constants;
import com.farm.base.record.ParamContent;
import com.farm.core.thirdparty.oss.OSSClientFactory;
import com.farm.core.thirdparty.oss.OSSPath;
import com.farm.core.thirdparty.oss.OSSPathBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 ** @Version 1.0.0
 */
public class ParamContentTest {


    @Test
    public void test(){



        ParamContent four = new ParamContent();
        four.setName("子类保存文字");
        four.setCreateTime("2019-05-01");
        four.setFlag("0");
        four.setValue("50");
        four.setType("1");
        four.setUnit("kg");
        four.setScore(3);


        ParamContent five = new ParamContent();
        five.setName("子类保存文字");
        five.setCreateTime("2019-05-01");
        five.setFlag("1");
        five.setValue("50");
        five.setType("1");
        five.setUnit("kg");
        five.setScore(3);

        List<ParamContent> listOne = new ArrayList<>();
        listOne.add(four);
        listOne.add(five);


        ParamContent one = new ParamContent();
        one.setName("保存图片");
        one.setCreateTime("2019-05-01");
        one.setFlag("1");
        one.setValue("a.jpg");
        one.setType("0");
        one.setUnit("");
        one.setScore(3);

        ParamContent two = new ParamContent();
        two.setName("保存文字");
        two.setCreateTime("2019-05-01");
        two.setFlag("1");
        two.setValue("30");
        two.setType("1");
        two.setUnit("米");
        two.setScore(3);


        ParamContent three = new ParamContent();
        three.setName("参入其他肥料");
        three.setCreateTime("2019-05-01");
        three.setFlag("1");
        three.setValue(listOne);
        three.setType("2");
        three.setUnit("米");
        three.setScore(3);


        List<ParamContent> listTwo = new ArrayList<>();

        listTwo.add(one);
        listTwo.add(two);
        listTwo.add(three);


        String json = JSONObject.toJSONString(listTwo);
//        System.out.println(json);


        List threeList = JSONObject.parseObject(json, List.class);

        Map<String, Object> map = countScore(threeList);
        Integer score = (Integer) map.get("score");
        String remark = (String) map.get("remark");

        Boolean f = finished(threeList);
        System.out.println(f);
        System.out.println(map.toString());

    }

    /**
     * 递归处理分数和扣分说明
     */

    private Map<String, Object> countScore(List list) {

        Map<String, Object> result = new HashMap<>();
        Integer count = 0;
        String remark = "";

        if (CollectionUtils.isEmpty(list)) {
            result.put("score", count);
            result.put("remark", remark);
            return result;
        }

        for (Object obj : list) {

            JSONObject jsonObject = (JSONObject) obj;
            ParamContent paramContent = jsonObject.toJavaObject(ParamContent.class);
            if ("0".equals(paramContent.getFlag()) && !("2".equals(paramContent.getType()))) {
                if (paramContent.getScore() != 0) {
                    String content = paramContent.getName() + " 未完成 扣 " + paramContent.getScore() + "分 ,";
                    remark += content;
                }
                continue;
            }

            if ("2".equals(paramContent.getType())) {
                //递归
                JSONArray value = (JSONArray) paramContent.getValue();
                List sunList = value.toJavaObject(List.class);
                Map<String, Object> map = countScore(sunList);
                Integer score = (Integer) map.get("score");
                String remarkStr = (String) map.get("remark");
                Boolean flagSun = (Boolean) map.get("flag");

                count += score;
                remark += remarkStr;

            } else {
                count += paramContent.getScore();
            }

        }

        result.put("score", count);
        result.put("remark", remark);
        return result;
    }


    /**
     * 判断农事环节是否全部完成
     */

    public Boolean finished(List list) {
        Boolean flag = true;

        for (Object obj : list) {
            JSONObject jsonObject = (JSONObject) obj;
            ParamContent paramContent = jsonObject.toJavaObject(ParamContent.class);
            if ("0".equals(paramContent.getFlag())) {
                flag = false;
                break;
            }

        }
        return flag;
    }

    @Test
    public void testUrl(){


        OSSPath formalOssPath = OSSPathBuilder.create().withDirectory(OSSClientFactory.Directory.FILES).
                withDataType(OSSClientFactory.DataType.IMAGE, "2019-04-30").withFilename("farm_20190430_164619.png").build();

//        String url = Constants.OSS_URL + formalOssPath.toString();

//        System.out.println(url);
    }


}