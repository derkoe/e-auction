(function($, angular) {
    var auction = angular.module('auction', []);

    auction.factory('auctionMessageService', [ '$rootScope', function($rootScope) {
        var socket = new SockJS('/auction');
        var stompClient = Stomp.over(socket);
        stompClient.connect('', '', function(frame) {
            stompClient.subscribe('/topic/auction', function(message) {
                var auctionUpdate = angular.fromJson(message.body);
                $rootScope.$broadcast('auctionUpdate', auctionUpdate);
            });
        });
        return {};
    } ]);

    auction.controller('AuctionCtrl', [ '$scope', 'auctionMessageService', function($scope, auctionMessageService) {
        $scope.auctionUpdate = {};

        $scope.$on('auctionUpdate', function(event, auctionUpdate) {
            $scope.auctionUpdate = auctionUpdate;
            $scope.$apply();
        });
    } ]);

}(jQuery, angular));