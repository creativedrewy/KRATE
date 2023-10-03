import UIKit
import SwiftUI
import shared
import GoogleSignIn

struct ComposeView: UIViewControllerRepresentable {
    var callback: () -> Void
    
    init(callback: @escaping () -> Void) {
        self.callback = callback
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        return Main_iosKt.MainViewController(createUIView: { () -> Void in
            self.callback()
        })
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

private class SwiftUIInUIView<Content: View>: UIView {
    
    init(content: Content) {
        super.init(frame: CGRect())

        let hostingController = UIHostingController(rootView: content)
        hostingController.view.translatesAutoresizingMaskIntoConstraints = false
        addSubview(hostingController.view)

        NSLayoutConstraint.activate([
            hostingController.view.topAnchor.constraint(equalTo: topAnchor),
            hostingController.view.leadingAnchor.constraint(equalTo: leadingAnchor),
            hostingController.view.trailingAnchor.constraint(equalTo: trailingAnchor),
            hostingController.view.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

struct ContentView: View {
    
    func handleSignInButton() {
        print("::: This was called from swift code!!!")
        
//      GIDSignIn.sharedInstance.signIn(
//        withPresenting: UIApplication.shared.delegate?.window??.rootViewController
//      ) { signInResult, error in guard let result = signInResult else {
//    
//            return
//          }
//    
//          // If sign in succeeded, display the app's main content View.
//        }
    }
    
    var body: some View {
        ComposeView(callback: handleSignInButton)
            .ignoresSafeArea(.all, edges: .all)
    }
}

