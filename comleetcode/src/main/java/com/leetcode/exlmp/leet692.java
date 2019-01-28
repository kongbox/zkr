package com.leetcode.exlmp;

import java.util.Arrays;

/**
 * Title:
 * Description:
 *
 * @author kdc
 * @version 1.0
 * @date 2019/1/16.17:06 首次创建
 * @date 2019/1/16.17:06 最后修改
 * @copyright 中科软科技股份有限公司
 */
public class leet692 {


    private int exection(int a, int b) {
        if (a < 1) {
            return 0;
        }
        //最大
        int maxNum = a * (a - 1) / 2;
        if (b > maxNum) {
            return 0;
        }


        return 0;
    }

     static void allSort(int[] array, int begin, int end) {
        //打印数组的内容
        if (begin == end) {
            int a= funtion(array);
            if(a==9) {
                System.out.println(Arrays.toString(array) + "--" + a);
            }
            return;
        }
        //把子数组的第一个元素依次和第二个、第三个元素交换位置
        for (int i = begin; i <= end; i++) {
            swap(array, begin, i);
            allSort(array, begin + 1, end);
            //交换回来
            swap(array, begin, i);

            int a= "".toCharArray().length;
        }
    }

    static void swap(int[] array, int a, int b) {
        int tem = array[a];
        array[a] = array[b];
        array[b] = tem;
    }

    static int funtion(int[] array){
        int n=0;
        for (int i = 0; i <array.length; i++) {
            for (int j = 0; j <array.length ; j++) {
                if (i>j&&array[i]<array[j]){
                        n++;
                }
            }
        }
        return n;
    }


    public static void main(String[] args) {
        int[] array = {1, 2, 3,4,5};
        allSort(array, 0, array.length - 1);

    }


    public int numFriendRequests(int[] ages) {
        int table[]=new int[121];
        int n=0;
        for(int A=1;A<=120;A++){
            for (int B = 0; B <ages.length ; B++) {
                if(A==B||ages[B] <= 0.5 *A + 7|| ages[B] > A||(ages[B] > 100 && A < 100)){

                }else{
                    table[A]++;
                }
            }
        }
        for (int B = 0; B <ages.length ; B++) {
            if(ages[B] <= 0.5 *ages[B] + 7|| ages[B] > ages[B]||(ages[B] > 100 && ages[B] < 100)){
                n=n+table[ages[B]];
            }else{
                n=n+table[ages[B]]-1;
            }
        }
        return n;
    }

    public int numFriendReque1sts(int[] ages) {
        int []nums = new int[121];
        int []sum = new int [121];
        int result = 0;
        for(int i = 0; i < ages.length; ++i){
            nums[ages[i]]++;
        }


        for(int i = 1; i < 121; ++i){
            sum[i] = sum[i - 1] + nums[i];
        }
        for(int i = 15; i < 121; ++i){
            if(nums[i] == 0) {
                continue;
            }
            int count = sum[i] - sum[i / 2 + 7] - 1;
            result += count * nums[i];
        }
        return result;
    }
}
