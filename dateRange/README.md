# Kata Date Range

## Scenarios

### Same month
Given a date range with start and end dates on the same month (for example : Nov.  9/Nov. 14)  
When I display the date range  
Then I should print "Between November 9 and 14"

### Different months
Given a date range with start and end dates on different months (for example : Oct.  9/to Nov. 14)   
When I display the date range  
Then I should print "Between October 9 and November 14"

### Differents years
Given a date range with start and end dates on different years (for example : Oct.  9, 2016/to Nov. 14, 2017)  
When I display the date range  
Then I should print "Between October 9, 2016 and November 14, 2017"

### End date is not defined and start date in the same year of current day
Given a date range with start defined at "a day in the month" (let say today is Nov. 1, 2016 and start date is Nov. 10, 2016)  
When I display the date range  
Then I should print "Thursday, the 10th of November"

### Otherwise
Given a date range with start defined at a day in another year (let say today is Nov. 1, 2017 and start date is Oct. 10, 2016)  
When I display the date range  
Then I should print "Thursday, the 10th of October 2016"

## Challenges

You should do the kata following TDD principles. It will ease the refactoring.

1. Do it with a lot of if
2. Do it without if (use functionnal approach, separate case between classes, ..."
3. Do you have a test for the 1st of November? The 2nd, ...
4. How will you do in case of i18n?

