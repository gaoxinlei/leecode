package com.example;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 基本排序算法test
 */
public class SortTest {

    private static  final Logger LOGGER = LoggerFactory.getLogger(SortTest.class);
    private Integer arr[];


    {
        Random r = new Random();
        arr = new Integer[50];
        for (int i=0;i<arr.length;i++){
            arr[i]=r.nextInt(100);
        }
    }

    /**
     * 冒泡排序
     * 每一轮将最大的冒泡到右侧.
     */
    @Test
    public  void testBubble(){
        List<Integer> list = Arrays.asList(arr);
        LOGGER.info("排序前:{}",list);
        for(int i=0;i<arr.length;i++){
            for(int j=0;j<arr.length-i-1;j++){
                int a = arr[j];
                int b = arr[j+1];
                if(a>b){
                    a = a^b;
                    b = a^b;
                    a = a^b;
                    arr[j] = a;
                    arr[j+1] = b;
                }
            }
        }
        LOGGER.info("排序后：{}",list
        );

    }

    /**
     * 选择排序。
     * 每一轮将最小的放置在左侧.
     */
    @Test
    public void testSelect(){
        List<Integer> list = Arrays.asList(arr);
        LOGGER.info("排序前：{}",list);
        for(int i=0;i<arr.length;i++){
            for (int j=i+1;j<arr.length;j++){
                int a = arr[i];
                int b = arr[j];
                if(a>b){
                    a = a^b;
                    b = a^b;
                    a = a^b;
                    arr[i] = a;
                    arr[j] = b;
                }
            }
        }
        LOGGER.info("排序后:{}",list);
    }

    /**
     * 插入排序.
     * 假定左侧子集有序.
     * 令i递增,令j负责决定i位置元素在0-i之间的正确位置.
     */
    @Test
    public void testInsert(){
        List<Integer> list = Arrays.asList(arr);
        LOGGER.info("排序前:{}",list);
        for(int i=1;i<arr.length;i++){
            for (int j=i;j>=1;j--){
                int a = arr[j];
                int b = arr[j-1];
                if(a<b){
                    a = a^b;
                    b = a^b;
                    a = a^b;
                    arr[j] = a;
                    arr[j-1] = b;
                }else{
                    break;
                }
            }
        }
        LOGGER.info("排序后:{}",list);
    }

    /**
     * 希尔排序,逐半分减.
     */
    @Test
    public void testShell(){
        List<Integer> list = Arrays.asList(arr);
        LOGGER.info("排序前:{}",list);
        int step = arr.length/2;
        while(step>0){
            for(int i=step;i<arr.length;i++){
                int target = arr[i];
                int index = i - step;
                while(index>=0 && arr[index] > target){
                    arr[index+step] = arr[index];
                    index-=step;
                }
                //退出while.
                arr[index+step] = target;
            }

            step = step/2;
        }
        LOGGER.info("排序后:{}",list);
    }

    /**
     * 归并排序.
     */
    @Test
    public void testMerge(){
        List<Integer> list = Arrays.asList(arr);
        LOGGER.info("排序前:{}",list);
         mergeSort(arr,0,arr.length-1);
        LOGGER.info("排序后:{}",Arrays.asList(arr));
    }

    private void mergeSort(Integer[] arr,int start,int end) {
        if(end>start){
            Integer[] result = new Integer[end-start+1];
            int mid = (start+end)/2;
            mergeSort(arr,start,mid);
            mergeSort(arr,mid+1,end);
            int left = start;
            int right = mid + 1;
            for(int i=0;i<result.length;i++){
                if(left <=mid && right<= end){
                    result[i] = arr[left]<=arr[right]?arr[left++]:arr[right++];
                    continue;
                }
                if(left>mid && right<=end){
                    result[i] = arr[right++];
                    continue;
                }
                if(right>end && left<=mid){
                    result[i] = arr[left++];
                    continue;
                }

                break;

            }
            System.arraycopy(result,0,arr,start,result.length);
        }

    }

    /**
     * 快速排序.
     */
    @Test
    public void testQuick(){
        List<Integer> list = Arrays.asList(arr);
        LOGGER.info("排序前:{}",list);
        quickSort(arr,0,arr.length-1);
        LOGGER.info("排序后:{}",list);

    }

    private void quickSort(Integer[] array,int begin,int end){
        if(end < begin+1 || begin<0){
            return;
        }
        //生成随机数分割数组.
        int pivot = (int)(begin + (end-begin+1)*Math.random());
        //小于pivot位置元素的一律放在左侧,大于等于的放在右侧.
        //先将pivot元素放在最右侧.
        swap(array,pivot,end);
        //遍历交换,找出适合分割的索引.
        int index = begin -1;//排序交换完成后小于pivot代表的元素的最大索引.
        for(int i=begin;i<=end;i++){
            //一定要小于等于.(保证出现前面大于,中间等于的情况下还能交换.)
            if(array[i]<=array[end]){
                //找到一个满足放在左侧(不大)的,index前进1.
                index++;
                //判断是否要交换,需要交换的条件是若干轮循环不满足arry[i]<=array[end]造成只增加i不增加index.
                //此时index经过新的自增指向第一个大于array[end]的元素,i指向最新发现的不大于array[end],交换.
                if(index < i){
                    swap(array,i,index);
                }
            }
        }
        //用索引分割.
        if(begin<index)
            quickSort(array,begin,index-1);
        if(index+1<end)
            quickSort(array,index+1,end);
    }

    private void swap(Integer[] array, int first, int second) {
        array[first] = array[first] ^ array[second];
        array[second] = array[first] ^ array[second];
        array[first] = array[first] ^ array[second];
    }


    /**
     * 堆排序.结果是小在前大在后.
     * 将参数数组看作堆,首先调成最大堆.
     * 利用最大堆的性质,将堆顶元素不停地向最后一个叶子节点交换,然后重新调整剩余部分为最大堆.
     * 直到全部调整完毕,仍旧为原来的索引,数组已为从小到大有序.
     */
    @Test
    public void testHeapSort(){
        List<Integer> list = Arrays.asList(arr);
        LOGGER.info("排序前:{}",list);
        heapSort(arr);
        LOGGER.info("排序后:{}",list);
    }



    /**
     * 堆排序算法
     *
     * @param array
     * @return
     */
    public  void heapSort(Integer[] array) {
        //记录调整堆时不应调整的限制(已排好顺序的部分)，初始记录数组array的长度；
        int limit = array.length;
        if (limit < 1) return ;
        //1.构建一个最大堆
        buildMaxHeap(array);
        //2.循环将堆首位（最大值）与末位交换，然后在重新调整最大堆
        while (limit > 0) {
            swap(array, 0, limit - 1);
            limit--;
            adjustHeap(array, 0,limit);
        }
    }
    /**
     * 建立最大堆
     *
     * @param array
     */
    private   void buildMaxHeap(Integer[] array) {
        //从最后一个非叶子节点开始向上构造最大堆
        for (int i = (array.length/2 - 1); i >= 0; i--) {
            adjustHeap(array, i,array.length);
        }
    }
    /**
     * 调整使之成为最大堆
     *
     * @param array
     * @param i
     */
    private  void adjustHeap(Integer[] array, int i,int limit) {
        int maxIndex = i;
        //如果有左子树，且左子树大于父节点，则将最大指针指向左子树
        if (i * 2 < limit && array[i * 2] > array[maxIndex])
            maxIndex = i * 2;
        //如果有右子树，且右子树大于父节点，则将最大指针指向右子树
        if (i * 2 + 1 < limit && array[i * 2 + 1] > array[maxIndex])
            maxIndex = i * 2 + 1;
        //如果父节点不是最大值，则将父节点与最大值交换，并且递归调整与父节点交换的位置。
        if (maxIndex != i) {
            swap(array, maxIndex, i);
            adjustHeap(array, maxIndex,limit);
        }
    }
}
