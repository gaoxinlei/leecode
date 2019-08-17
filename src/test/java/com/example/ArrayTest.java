package com.example;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数组.
 */
public class ArrayTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayTest.class);

    /**
     * 两数之和.
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     * <p>
     * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
     */
    @Test
    public void testTwoSum() {
        int[] arr = new int[]{1, 5, 9, -2, 7, 4};
        int[] sum = twoSum(arr, 7);
        LOGGER.info("result:{},{}", sum[0], sum[1]);
    }

    private int[] twoSum(int[] nums, int target) {
        if (null == nums || nums.length < 2) {
            return null;
        }
        Map<Integer, List<Integer>> indexMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int e = nums[i];
            indexMap.computeIfAbsent(e, (x) -> new ArrayList<>());
            indexMap.get(e).add(i);
        }
        int result[] = new int[2];
        for (int i = 0; i < nums.length; i++) {
            int e = nums[i];
            int t = target - e;
            List<Integer> indexes = indexMap.getOrDefault(t, new ArrayList<>());
            if (e != t && indexes.size() > 0) {
                result[0] = i;
                result[1] = indexes.get(0);
                return result;
            }
            if (e == t && indexes.size() > 1) {
                result[0] = indexes.get(0);
                result[1] = indexes.get(1);
                return result;
            }
        }
        return result;
    }

    /**
     * 两数相加.
     * 此题可转换为面试题,两个无限大的整数字符串求和.
     * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
     * <p>
     * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
     * <p>
     * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头
     */
    @Test
    public void testAddTwoNumbers() {
        ListNode l1 = new ListNode(342);
        ListNode l2 = new ListNode(465);
        LOGGER.info("相加结果:{}", addTwoNumbers(l1, l2).calculateValue());
    }

    private class ListNode {
        int value;
        ListNode next;

        //空构造函数过题.
        ListNode() {

        }

        ListNode(int initValue) {
            //只考虑正数.
            value = initValue % 10;
            int nextValue = initValue / 10;
            if (nextValue > 0) {
                this.next = new ListNode(nextValue);
            }
        }

        //以当前node为个位向前计算.
        int calculateValue() {
            int result = this.value;
            int exe = 1;
            ListNode node = this;
            while ((node = node.next) != null) {
                result += node.value * Math.pow(10, exe++);
            }
            return result;
        }
    }

    private ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        //为了能过官方,同时也规避value过大的情况,不这么玩.
//        int value = l1.calculateValue() + l2.calculateValue();
////        ListNode result = new ListNode(value);
        if (l1 == null && l2 == null)
            return new ListNode();
        ListNode result = new ListNode();
        ListNode current = result;
        boolean plus = false;
        //因为是逆序,因此可以直接从首位向后加.
        while (true) {
            int value = 0;
            value = l1 != null ? value + l1.value : value;
            value = l2 != null ? value + l2.value : value;
            value = plus ? value + 1 : value;
            current.value = value % 10;
            if (value / 10 > 0)
                plus = true;
            else
                plus = false;
            l1 = l1.next;
            l2 = l2.next;
            if (l1 != null || l2 != null || plus) {
                ListNode next = new ListNode();
                current.next = next;
                current = next;
            } else
                break;

        }
        return result;
    }

    /**
     * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
     * <p>
     * 示例 1:
     * <p>
     * 输入: "abcabcbb"
     * 输出: 3
     * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
     * 示例 2:
     * <p>
     * 输入: "bbbbb"
     * 输出: 1
     * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
     * 示例 3:
     * <p>
     * 输入: "pwwkew"
     * 输出: 3
     * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
     *      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
     */
    @Test
    public void testLengthOfLongestSubstring() {
        //采用这种方式会超出时间,猜测原因为复杂度o(n2)的原因.
//        LOGGER.info("最大无重复子串长度:{}", lengthOfLongestSubstringByDivid("ynqogshxhchhpqhjrwwtdm"));
        LOGGER.info("最大无重复子串长度:{}", lengthOfLongestSubstring("ynqogshxhchhpqhjrwwtdm"));
    }

    //改换动态规划.
    private int lengthOfLongestSubstring(String s) {
        //思想是从左侧开找,大问题为f,找到第一个不重复的子串,分解为转移公式f(s) = max(f(s1),f(s2));
        //其中若s为ynqogshxhchhpqhjrwwtdm.
        //则s1很明显为ynqogshx,而s2则为xhchhpqhjrwwtdm,前者和后者是有交集的,起止点在于重复的字符'h'.
        //因此,对每个子串进行一个hashmap保持索引,完成最大长度计算并找出前后索引后即可释放.
        //边界条件f(s0)=1,即只有单个字母的长度.
        //本方式中有一个大弊端,每个子串都会占用一块内存,下一步的改进应当是将s转为数组并每次传递相应索引.
        if(s==null||s.length()==0){
            return 0;
        }
        if(s.length() == 1){
            return 1;
        }
        CharIndexBean bean = findCharIndex(s);
        //因为前面有验证,若bean存在则代表要侵害,否则代表s自身就是无重复串.
        if(null == bean)
            return s.length();
        //前面保证了s必然有2个及以上长度,以prev+1代表f(s2)的子串,next-1代表f(s1),比较他们的长度.
        return Math.max(bean.next,lengthOfLongestSubstring(s.substring(bean.prev+1)));
    }

    private CharIndexBean findCharIndex(String s) {
        //暂不考虑空.
        if(s==null||s.length()==0){
            return null;
        }
        CharIndexBean bean = new CharIndexBean();
        if(s.length() == 1){
            bean.c = s.charAt(0);
        }
        Map<Character,Integer> map = new HashMap<>();
        for(int i=0;i<s.length();i++){
            char a = s.charAt(i);
            if(null!=map.putIfAbsent(a,i)){
                Integer prev = map.get(a);
                int next = i;
                bean.c = a;
                bean.prev = prev;
                bean.next = next;
                return bean;
            }
        }
        return null;
    }

    private class CharIndexBean{
        char c;
        int prev;
        int next;
    }

    //分割方式去查最大无重子串,自测跑通,但执行时间超时,
    //方式为从两侧逐渐向内,算法是n2级别.
    private int lengthOfLongestSubstringByDivid(String s) {
        if (null == s || s.isEmpty()) {
            return 0;
        }
        //用它来记录字符串和长度的键值对,因为最大无重复子串长度一定大于1,此处记录使用0表示有重复.
        //那么可以省去重复的子串判断有重字符或者无重字符的情况下的长度.
        Map<String, Integer> result = new HashMap<>();

        int length = getLongestLength(s, result);
        if (length > 0) {
            return length;
        }
        //不会有小于1的.
        return 1;
    }

    int getLongestLength(String s, Map<String, Integer> result) {
        //计算当前参数字符串是否有重复.
        int length = findLength(s, result);
        if (length > 0) {
            //length大于0代表无重,返回.
            return length;
        } else if (s.length() > 1) {
            //分别计算或获取已计算的左子串和右子串长度(或0代表有重复).
            String left = s.substring(0, s.length() - 1);
            int leftLen = findLength(left);
            String right = s.substring(1, s.length());
            int rightLen = findLength(right);
            if(leftLen == 0 && rightLen == 0){
                //左右子串都有重复.分别找子串.
                rightLen = getLongestLength(right, result);
                leftLen = getLongestLength(left, result);
                //可能最终还是得到两个0.
                return Math.max(leftLen, rightLen);
            }
            //至少一个无重复,直接计算大值.
            return Math.max(leftLen,rightLen);
        }
        return 0;
    }


    //有重复,返0,无重复,返长度.
    private int findLength(String s, Map<String, Integer> map) {
        return map.containsKey(s) ? map.get(s) : findLength(s);
    }

    private int findLength(String s) {
        Set<Character> set = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            set.add(s.charAt(i));
        }
        return set.size() == s.length() ? s.length() : 0;
    }


    /**
     * 给定两个大小为 m 和 n 的有序数组 nums1 和 nums2。
     *
     * 请你找出这两个有序数组的中位数，并且要求算法的时间复杂度为 O(log(m + n))。
     *
     * 你可以假设 nums1 和 nums2 不会同时为空。
     *
     */
    @Test
    public void testFindMedianSortedArrays(){
        //1 3 6 9 12
        //2 14 15 18
        //思路,转换为要求出两个数组合并后的第几小,如例子中要查出第5小.
        //则每次排除它的一半,即首次从中去除掉不可能是第5小的2个元素,下一轮,再找剩下的第3小.
        double result = findMedianSortedArrays(new int[]{1,3,6,9,12},new int[]{2,14,15,18});
        LOGGER.info("数组中位数:{}",result);
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if(nums1.length == 0 && nums2.length == 0)
            return 0.0;
        //有一个空的直接换作对另一个的查找.
        if(nums1.length==0)
            return findMedian(nums2);
        if(nums2.length==0)
            return findMedian(nums1);
        return findMedianSortedArrays(nums1,0,nums2,0,nums1.length+nums2.length);

    }

    private double findMedianSortedArrays(int[] nums1, int firstIndex, int[] nums2, int secondIndex,int totalLength) {
        int target  = totalLength%2==0?totalLength/2+1:totalLength/2;
        int first = nums1[firstIndex+target];
        int second = nums2[secondIndex+target];
        //忽略.
        return 0;
    }

    private double findMedian(int[] nums) {
        int len = nums.length;
        if(len==0){
            return 0.0;
        }
        if(len%2==0){
            return ((double)nums[len/2]+(double)nums[len/2+1])/2;
        }
        return (double)(nums[len/2]);
    }

    /**
     * 在一个数组中找出三个相加为0的元素.
     */
    @Test
    public void testThreeSum() {
        int[] nums = {-1, 0, 1, 2, -1, -4};
        LOGGER.info("结果:{}", threeSum(nums));
    }

    private List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (null == nums || nums.length < 3) {
            return result;
        }
        //1.排序.
        Arrays.sort(nums);
        //3.将去重排序的数组进行验证.
        int min = nums[0];
        int max = nums[nums.length - 1];
        if (min + max < min || min + max > max) {
            //说明min max同号,不用查找了.
            return result;
        }
        //4.经过上面的校验,可以开始查找,用双指针.
        int index = 0;
        int indexValue;
        while (index < nums.length - 1 && (indexValue = nums[index]) <= 0) {
            //外循环一轮次索引加1.
            int first = index + 1;
            int end = nums.length - 1;
            while (first < end) {

                int firstValue = nums[first];
                int endValue = nums[end];
                int mid = firstValue + endValue;
                if (mid == -indexValue) {
                    //first end相加的值匹配,加入结果集.
                    List<Integer> item = new ArrayList<>();
                    result.add(item);
                    item.add(indexValue);
                    item.add(firstValue);
                    item.add(endValue);
                    //不能break,而是应将first和end都向内进一步.
                    first = advanceFirstIndex(first, nums);
                    end = fallbackEndIndex(end, nums);
                    //下面这两个else让我纠结许久,其实就是不等式传递的知识,确实每次只需要移动一个指针就够了.
                    //使用前进索引的方式,去重复结果.
                } else if (mid < -indexValue) {
                    first = advanceFirstIndex(first, nums);
                } else {
                    end = fallbackEndIndex(end, nums);
                }
            }
            //前进index找下一轮.
            index = advanceFirstIndex(index, nums);
        }
        return result;
    }

    private int fallbackEndIndex(int end, int[] nums) {
        //找数组下一个后方不同值的位置.
        int currentValue = nums[end];
        int endIndex = end - 1;
        while (endIndex > 0 && nums[endIndex] == currentValue) {
            endIndex--;
        }
        return endIndex;
    }

    private int advanceFirstIndex(int first, int[] nums) {
        //找数组下一个前方不同值的位置.
        int currentValue = nums[first];
        int firstIndex = first + 1;
        while (firstIndex < nums.length && nums[firstIndex] == currentValue) {
            firstIndex++;
        }
        return firstIndex;
    }

}
