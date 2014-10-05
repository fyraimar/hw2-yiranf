#include <iostream>
#include <fstream>
#include <string>
#include <map>

using std::string;
using std::multimap;
using std::pair;



int main() {
    std::ifstream mydata;
    std::ifstream stddata;

    stddata.open("sample.out");

    int total_ans = 0;

    multimap<string, string> ans;
    std::string tmp;
    while (std::getline(stddata, tmp)) {
        string key = tmp.substr(0, 14);
        ans.insert(std::pair<string, string>(key, tmp));
        total_ans++;
    }

    stddata.close();

    std::multimap<string, string>::iterator it;

    int total_atempt = 0;
    int total_correct = 0;

    mydata.open("out");
    while (std::getline(mydata, tmp)) {
        total_atempt++;
        string key = tmp.substr(0, 14);

        std::pair <std::multimap<string, string>::iterator, std::multimap<string ,string>::iterator> ret;
        ret = ans.equal_range(key);

        for (multimap<string ,string>::iterator it = ret.first; it != ret.second; ++it) {
            if (it->second == tmp) {
                total_correct++;
                break;
            }
        }
    }
    mydata.close();

    double acc = (double)total_correct/total_atempt;
    double recall = (double)total_correct/total_ans;

    double f1 = 2*(acc*recall)/(acc+recall);

    std::cout << "Acc: " << acc << std::endl << "Rel: " << recall << std::endl;
    std::cout << "F1: " << f1 << std::endl;

    return 0;
}
