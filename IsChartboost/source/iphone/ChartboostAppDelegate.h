#import <UIKit/UIKit.h>

@class ChartboostViewController;

@interface ChartboostAppDelegate : UIResponder <UIApplicationDelegate>

@property (retain, nonatomic) UIWindow *window;

@property (retain, nonatomic) ChartboostViewController *viewController;

@end

namespace IsChartboostUtil
{
	void IsChartboostReqestAd();
	static bool IsChartboostIsRequestingAd();
	static void IsChartboostResponseCB(int response);
	static void IsChartboostDismissCB(NSString * location);
	static void IsChartboostClicked(NSString * location);
	static void IsChartboostClosed(NSString * location);

};

