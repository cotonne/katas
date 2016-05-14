# Do It Like Google
## Description
Here, you have to part. The first is aimed for the developer, the second for tester, even if both should play all parts.

The aim is to have a lot of questions from your assembly.
They have to think of pitfalls, problems and contraints.
In fact, they should not even write one line of code.

## Developer
### Duration
Around 15 - 20 min
### Question
You have to write the following function:
```C
acount(void* x);
```

If you are not familiar with C, you can try Java:
```Java
acount(char[] x);
```

### Expected questions
- What is the aim of this function? (should be the first one :) )
- Does something like this already exists somewhere else?
- Why only a?
- a or A?
- What is the encoding? You can have a lot of differents hyphens
- Can we improved the name of the function? of the parameter?
- Can we change the type of the parameter? char* with C and String/InputStream for Java?
- How "big" can be the input? With Google, you will need a int64.
- Can we trust the input? 
- The input can be null? if not, you can avoid a Null test
- How often this function will be called? Maybe we have to work on speed
- Maybe we should implement it with a MapReduce and parallize the processing?
- Is the data already sorted? If so, you can count until the first b.
- For C: is this a null terminated string?
- Should we expect some errors? What would be the error code?
- Does the count should be absolutly accurate? Maybe, with a sample, we can determine the frequency?

## Tester
_Incoming, will be a same web page to test_
