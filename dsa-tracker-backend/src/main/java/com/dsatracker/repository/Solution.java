package com.dsatracker.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Solution {
	public static void main(String[] args) {
		int[] nums = {13,25,83,77};
		Solution solution = new Solution();
		int[] result = solution.separateDigits(nums);
		System.out.println("result: " + Arrays.toString(result));
	}
 	 public int[] separateDigits(int[] nums) {
		 List<Character> list = new ArrayList<>();
		 for(int i : nums) {
			 for(char c : String.valueOf(i).toCharArray()) {
				 list.add(c);
			 }
		 }
		 // convert List<Character> to int[] by converting digit characters to numeric values
		 int[] result = new int[list.size()];
		 for (int i = 0; i < list.size(); i++) {
			 result[i] = Character.getNumericValue(list.get(i));
		 }
		 System.err.println("Character list: " + Character.getNumericValue('z'));
		 return result;
	 }
}