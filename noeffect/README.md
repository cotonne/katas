This kata is inspired from http://arlobelshee.com/mock-free-example-part-3-fixing-untestable-code-sequences/.

#Steps
## Fix primitive obsession
Regroup some variables in a new type:
- name
- id
- math

Extract toColor and toDetails to a formatter class

Replace for(int temp ...) with foreach

## Factorize the code
Regroup code into functions
Give meaningful names

You should start to see the structure
## Write unit tests

## Replace Semicolons With Data Flow
 We change this:

FirstThing();
SecondThing();
return _fieldsThatHaveBeenModifiedByTheAbove;
into this:

var data = new AllOfTheStateForAllSteps();
data = FirstThing(data);
data = SecondThing(data);
return data.FinalResult;

## Use monad and immutables 
we are turning this:
var data = new AllOfTheStateForAllSteps();
data = FirstThing(data);
data = SecondThing(data);
return data.FinalResult;

into this:

IEnumerable<Func<AllOfTheStateForAllSteps, AllOfTheStateForAllSteps>>
  steps = new[] {FirstThing, SecondThing};
var data = new AllOfTheStateForAllSteps();
foreach(var op in steps)
{
  data = op(data);
}
return data.FinalResult;

into this:

var pipeline = DecideWhatToDo();
 
var data = new AllOfTheStateForAllSteps();
state = pipeline.Aggregate(state, (current, op) => op(current));
return data.FinalResult;