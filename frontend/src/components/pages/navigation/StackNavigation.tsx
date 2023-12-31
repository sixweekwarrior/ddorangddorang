import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import NavBar from '../../../components/atoms/navbar';
import Onboarding from '../../../components/pages/onboarding';
import Enter from '../../../components/pages/enter';
import EnterWait from '../enterWait';
import BeforeStart from '../beforeStart';
import MakeRoom from '../makeRoom';
import WaitList from '../waitList';
import MyGroup from '../myGroup';
import MissionToday from '../missionToday';
import {GoMission} from '../goMission';
import ReviseInfo from '../reviseInfo';
import AdditionalInfo from '../additionalInfo';
import BasicInfo from '../basicInfo';
import ReviseToday from '../reviseToday';
import ReviseSsafy from '../reviseSsafy';
import ReviseEtc from '../reviseEtc';
import SendOpinion from '../sendOpinion';
import MatchStatus from '../matchStatus';
import MatchGuess from '../matchGuess';
import MyPage from '../myPage';
import {ProfilePicAdd} from '../profilePicAdd';
import ChattingDeatil from '../chattingDetail';

const StackNavigation = () => {
  const Stack = createStackNavigator();
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Onboarding">
        <Stack.Screen
          name="Onboarding"
          component={Onboarding}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="Enter"
          component={Enter}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="MakeRoom"
          component={MakeRoom}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="EnterWait"
          component={EnterWait}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="BeforeStart"
          component={BeforeStart}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="NavBar"
          component={NavBar}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="WaitList"
          component={WaitList}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="MyGroup"
          component={MyGroup}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="MissionToday"
          component={MissionToday}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="GoMission"
          component={GoMission}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="ReviseInfo"
          component={ReviseInfo}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="MyPage"
          component={MyPage}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="ProfilePicAdd"
          component={ProfilePicAdd}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="BasicInfo"
          component={BasicInfo}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="AdditionalInfo"
          component={AdditionalInfo}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="ReviseToday"
          component={ReviseToday}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="ReviseSsafy"
          component={ReviseSsafy}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="ReviseEtc"
          component={ReviseEtc}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="SendOpinion"
          component={SendOpinion}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="MatchStatus"
          component={MatchStatus}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="MatchGuess"
          component={MatchGuess}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="ChattingDeatil"
          component={ChattingDeatil}
          options={{headerShown: false}}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default StackNavigation;
