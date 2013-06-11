#import "ChartboostViewController.h"
#import "Chartboost.h"
#include "s3eDevice.h"
@interface ChartboostViewController () <ChartboostDelegate>
@end

@implementation ChartboostViewController

- (IBAction)showInterstitial:(id)sender {
	
	[[Chartboost sharedChartboost] showInterstitial];
	
}

- (IBAction)showInterstitialMainMenu:(id)sender {
    // This will show an interstitial for the Main Menu location
    // This will first check to see if an interstitial is cached for that location, if so it will display it. If no cache exists for this location - it will retrieve an interstitial from the server
    [[Chartboost sharedChartboost] showInterstitial:@"Main Menu"];
    
}

- (IBAction)showInterstitialLevelOne:(id)sender {
    // This will show an interstitial for the After level 1 location
    [[Chartboost sharedChartboost] showInterstitial:@"Game Over"];
    
}

- (IBAction)showMoreApps:(id)sender {
    [[Chartboost sharedChartboost] showMoreApps];
}

- (void)viewDidLoad
{
	
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
    } else {
        return YES;
    }
}

@end
